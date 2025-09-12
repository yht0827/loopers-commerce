package com.loopers.domain;

import static com.loopers.support.ranking.RankingType.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.loopers.config.event.ProductAggregationEvent;
import com.loopers.support.RankingEventMapper;
import com.loopers.support.ranking.RankingEventType;
import com.loopers.support.ranking.RankingKeyManger;
import com.loopers.support.ranking.RankingScoreCalculator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingService {

	private final StringRedisTemplate redisTemplate;
	private final RankingKeyManger rankingKeyManger;
	private final RankingScoreCalculator scoreCalculator;
	private final ProductMetricsRepository productMetricsRepository;

	public void handleAggregateScores(List<ConsumerRecord<String, ProductAggregationEvent>> records) {
		if (records == null || records.isEmpty()) {
			return;
		}

		try {
			// 1. 이벤트를 날짜별, 상품별로 그룹화
			Map<LocalDate, Map<Long, List<ProductAggregationEvent>>> eventsByDateAndProduct = groupEventsByDateAndProduct(
				records);

			// 2. 이벤트 배치 업데이트
			for (Map.Entry<LocalDate, Map<Long, List<ProductAggregationEvent>>> dateEntry : eventsByDateAndProduct.entrySet()) {
				LocalDate date = dateEntry.getKey();
				Map<Long, List<ProductAggregationEvent>> productEvents = dateEntry.getValue();

				updateDailyRanking(date, productEvents);
			}

			log.debug("랭킹 점수 업데이트 완료 - 날짜별 그룹: {}", eventsByDateAndProduct.size());

		} catch (Exception e) {
			log.error("랭킹 점수 집계 처리 실패", e);
			throw e;
		}
	}

	public Map<LocalDate, Map<Long, List<ProductAggregationEvent>>> groupEventsByDateAndProduct(
		List<ConsumerRecord<String, ProductAggregationEvent>> records) {

		return records.stream()
			.filter(record -> record.value() != null)
			.map(ConsumerRecord::value)
			.filter(event -> event.productId() != null && event.eventDate() != null)
			.collect(Collectors.groupingBy(ProductAggregationEvent::eventDate, // 날짜별 그룹핑
				Collectors.groupingBy(ProductAggregationEvent::productId) // 상품별 그룹핑
			));
	}

	public void updateDailyRanking(LocalDate date, Map<Long, List<ProductAggregationEvent>> productEvents) {
		String rankingKey = rankingKeyManger.getDailyRankingKey(date);

		// 3. Redis 파이프라인을 사용한 배치 처리
		redisTemplate.executePipelined((RedisCallback<?>)(connection) -> {
			for (Map.Entry<Long, List<ProductAggregationEvent>> productEntry : productEvents.entrySet()) {
				Long productId = productEntry.getKey();
				List<ProductAggregationEvent> events = productEntry.getValue();

				// 4. 이벤트별 점수 계산
				double totalScore = calculateBatchEventScore(events);

				if (totalScore != 0.0) {
					// 5. 타이브레이커 적용
					double finalScore = applyTieBreaker(totalScore, productId);

					// 6. Redis ZSet에 점수 누적 (ZINCRBY)
					connection.zIncrBy(rankingKey.getBytes(), finalScore, productId.toString().getBytes());

					log.trace("랭킹 점수 업데이트 - productId: {}, score: {}, finalScore: {}", productId, totalScore, finalScore);
				}
			}

			// TTL 설정
			Duration ttl = Duration.ofSeconds(rankingKeyManger.getTTL(DAILY));
			connection.expire(rankingKey.getBytes(), ttl.getSeconds());

			return null; // 파이프라인 콜백은 null 반환
		});

		log.debug("파이프라인 배치 업데이트 완료 - rankingKey: {}, products: {}", rankingKey, productEvents.size());
	}

	public double calculateEventScore(ProductAggregationEvent event) {
		if (event == null || event.eventType() == null || event.occurredAt() == null) {
			return 0.0;
		}

		try {

			// 이벤트 타입에 따른 기본 점수 계산
			RankingEventType rankingEventType = RankingEventMapper.toRankingEventType(event.eventType());

			// 고정 가중치
			double baseScore = scoreCalculator.calculateScore(rankingEventType);

			// LIKE & UNLIKE  액션 배수
			double actionScore = baseScore * event.getScoreMultiplier();

			// 시간 감쇠 적용 (최근일수록 높은 점수)
			double finalScore = scoreCalculator.applyTimeDecay(actionScore, event.occurredAt());

			log.trace("랭킹 점수 계산 처리 성공 - eventType: {}, baseScore: {}, actionScore: {}, finalScore: {}", event.eventType(),
				baseScore, actionScore, finalScore);

			return finalScore;

		} catch (Exception e) {
			log.error("랭킹 점수 계산 처리 실패", e);
		}

		return 0.0;
	}

	public double calculateBatchEventScore(List<ProductAggregationEvent> events) {
		if (events == null || events.isEmpty()) {
			return 0.0;
		}

		// 유효하지 않은 이벤트 필터링 및 점수 계산을 한 번에 처리
		return events.stream()
			.filter(event -> event != null && event.eventType() != null && event.occurredAt() != null)
			.mapToDouble(this::calculateEventScore)
			.sum();
	}

	public double applyTieBreaker(double baseScore, Long productId) {
		// productId 역순으로 아주 작은 가중치 추가 (동점 시 큰 ID가 높은 순위)
		double tieBreaker = (Long.MAX_VALUE - productId) * 0.000000001;
		return baseScore + tieBreaker;
	}

	public void carryOverRankingScores() {
		String todayKey = rankingKeyManger.getDailyRankingKey(LocalDate.now());
		String tomorrowKey = rankingKeyManger.getDailyRankingKey(LocalDate.now().plusDays(1));

		redisTemplate.opsForZSet().unionAndStore(todayKey, Collections.emptySet(), tomorrowKey);

		Duration ttl = Duration.ofSeconds(rankingKeyManger.getTTL(DAILY));
		redisTemplate.expire(tomorrowKey, ttl);

		log.info("랭킹 점수 이월 완료: {} -> {}", todayKey, tomorrowKey);
	}
}
