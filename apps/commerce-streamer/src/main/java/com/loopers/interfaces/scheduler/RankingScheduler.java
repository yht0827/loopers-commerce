package com.loopers.interfaces.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.loopers.domain.RankingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingScheduler {

	private final RankingService rankingService;

	/**
	 * 매일 23시 50분에 전날의 랭킹 데이터를 다음날 랭킹으로 이월
	 */
	@Scheduled(cron = "0 50 23 * * ?") // 23시 50분 실행
	public void carryOverScores() {
		log.info("랭킹 점수 스케줄러 실행");
		rankingService.carryOverRankingScores();
		log.info("랭킹 점수 스케줄러 완료");
	}

}
