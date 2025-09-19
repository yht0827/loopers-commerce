package com.loopers.interfaces.scheduler;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.loopers.domain.metrics.RankingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingScheduler {

	private final RankingService rankingService;
	private final JobLauncher jobLauncher;
	private final Job dailyRankingJob;

	/**
	 * 매일 23시 50분에 전날의 랭킹 데이터를 다음날 랭킹으로 이월
	 */
	@Scheduled(cron = "0 50 23 * * ?") // 23시 50분 실행
	public void carryOverScores() {
		log.info("랭킹 점수 스케줄러 실행");
		rankingService.carryOverRankingScores();
		log.info("랭킹 점수 스케줄러 완료");
	}

	// 매일 00:10, KST (전일 기준 실행)
	@Scheduled(cron = "0 10 0 * * *", zone = "Asia/Seoul")
	public void runDaily() {
		LocalDate target = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
		run(target, 100, false);  // topN=100, dryRun=false
	}

	// 필요할 때 수동 호출
	public void run(LocalDate date, int topN, boolean dryRun) {
		JobParameters params = new JobParametersBuilder()
			.addString("date", date.toString())
			.addLong("topN", (long)topN)
			.addString("dryRun", Boolean.toString(dryRun))
			.toJobParameters();
		try {
			jobLauncher.run(dailyRankingJob, params);
		} catch (Exception e) {
			log.error("일간 랭킹 배치 실패 (date={}, topN={}, dryRun={})", date, topN, dryRun, e);
		}
	}

}
