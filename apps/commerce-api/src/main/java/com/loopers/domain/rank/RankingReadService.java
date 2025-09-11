package com.loopers.domain.rank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.support.ranking.RankingKeyManger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingReadService {

	private final RankingValidator rankingValidator;
	private final StringRedisTemplate redisTemplate;
	private final RankingKeyManger rankingKeyManger;

	/**
	 * 랭킹 페이지 조회
	 */
	public Page<RankingItem> getRanking(final LocalDate date, final Pageable pageable) {
		if (pageable == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "페이지는 비어 있을수 없습니다.");
		}
		// 날짜 검증
		LocalDate validatedDate = rankingValidator.validateDate(date);

		// 키 생성
		String key = rankingKeyManger.getDailyRankingKey(validatedDate);

		// 전체 개수 조회
		Long total = redisTemplate.opsForZSet().zCard(key);
		if (total == null || total == 0) {
			return Page.empty(pageable);
		}

		// 페이징 계산
		int start = pageable.getPageNumber() * pageable.getPageSize();
		int end = start + pageable.getPageSize() - 1;

		// 상위 랭킹 조회 (점수 높은 순)
		Set<ZSetOperations.TypedTuple<String>> rankedProducts = redisTemplate.opsForZSet()
			.reverseRangeWithScores(key, start, end);

		if (start >= total || rankedProducts == null || rankedProducts.isEmpty()) {
			return new PageImpl<>(List.of(), pageable, total);
		}

		// 랭킹 데이터 구성
		List<RankingItem> items = toItems(rankedProducts, start);

		return new PageImpl<>(items, pageable, total);
	}

	public List<RankingItem> toItems(Set<ZSetOperations.TypedTuple<String>> rankedProducts, int start) {
		if (rankedProducts == null || rankedProducts.isEmpty()) {
			return List.of();
		}

		List<RankingItem> items = new ArrayList<>(rankedProducts.size());
		long rankBase = start + 1;

		for (ZSetOperations.TypedTuple<String> t : rankedProducts) {
			Long productId;
			try {
				productId = t.getValue() == null ? null : Long.parseLong(t.getValue());

			} catch (NumberFormatException e) {
				log.warn("Invalid productId in ranking: {}", t.getValue());
				continue;
			}

			Double score = t.getScore();
			if (productId == null || score == null)
				continue;

			items.add(RankingItem.create().rank(rankBase++).productId(productId).score(score).build());
		}

		return items;
	}
}
