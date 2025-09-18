package com.loopers.interfaces.scheduler.ranking;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyRankingJobScheduler {

	private final JobLauncher jobLauncher;

	@Qualifier("dailyRankingJob")
	private final Job dailyRankingJob;

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
			log.error("dailyRankingJob failed (date={}, topN={}, dryRun={})", date, topN, dryRun, e);
		}
	}
}

