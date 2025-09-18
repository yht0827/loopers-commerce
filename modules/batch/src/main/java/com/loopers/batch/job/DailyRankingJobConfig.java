package com.loopers.batch.job;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DailyRankingJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job dailyRankingJob(Step dailyRankingStep) {
		return new JobBuilder("dailyRankingJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(dailyRankingStep)
			.build();
	}

	@Bean
	public Step dailyRankingStep(
		ItemReader<ProductScoreRow> dailyReader,
		ItemProcessor<ProductScoreRow, ProductScoreRow> dailyProcessor,
		ItemWriter<ProductScoreRow> dailyWriter
	) {
		return new StepBuilder("dailyRankingStep", jobRepository)
			.<ProductScoreRow, ProductScoreRow>chunk(100, transactionManager)
			.reader(dailyReader)
			.processor(dailyProcessor)
			.writer(dailyWriter)
			.faultTolerant() // 필요 시 skip/retry 추가
			.build();
	}

	@Bean
	@StepScope
	public JdbcCursorItemReader<ProductScoreRow> dailyReader(
		DataSource dataSource,
		@Value("#{jobParameters['date']}") String dateStr,
		@Value("#{jobParameters['topN']}") Integer topN
	) {
		String sql = """
			SELECT product_id, score
			FROM product_metrics_daily
			WHERE rank_date = ?
			ORDER BY score DESC, product_id DESC
			LIMIT ?
			""";

		JdbcCursorItemReader<ProductScoreRow> r = new JdbcCursorItemReader<>();
		r.setDataSource(dataSource);
		r.setSql(sql);
		r.setPreparedStatementSetter(ps -> {
			ps.setDate(1, java.sql.Date.valueOf(LocalDate.parse(dateStr)));
			ps.setInt(2, topN != null ? topN : 100);
		});
		r.setRowMapper((rs, rowNum) -> new ProductScoreRow(
			rs.getLong("product_id"), rs.getDouble("score")));
		return r;
	}

	@Bean
	@StepScope
	public ItemProcessor<ProductScoreRow, ProductScoreRow> dailyProcessor() {
		return item -> item;
	}

	/**
	 * 멱등 Writer: 대상 date 선삭제 후 순위 번호 증가하며 일괄 INSERT.
	 * dryRun=true면 쓰기 생략하고 로그만 출력
	 */
	@Bean
	@StepScope
	public ItemWriter<ProductScoreRow> dailyWriter(
		NamedParameterJdbcTemplate jdbc,
		@Value("#{jobParameters['date']}") String dateParam,
		@Value("#{jobParameters['dryRun'] ?: false}") boolean dryRun
	) {
		return new ItemWriter<>() {
			private boolean deleted = false;
			private int nextRank = 1;

			private static final String DELETE_SQL =
				"DELETE FROM product_rank_daily WHERE rank_date = :date";
			private static final String INSERT_SQL = """
				INSERT INTO product_rank_daily (rank_date, product_id, rank_no, score)
				VALUES (:date, :productId, :rankNo, :score)
				""";

			@Override
			public void write(Chunk<? extends ProductScoreRow> chunk) {
				if (chunk == null || chunk.isEmpty())
					return;

				LocalDate date = LocalDate.parse(dateParam);
				Date sqlDate = Date.valueOf(date);

				if (dryRun) {
					log.info("[DRY-RUN] date={}, would write {} items (rank {}..{})",
						date, chunk.size(), nextRank, nextRank + chunk.size() - 1);
					nextRank += chunk.size();
					return;
				}

				if (!deleted) {
					int del = jdbc.update(DELETE_SQL,
						new MapSqlParameterSource().addValue("date", sqlDate));
					log.info("cleared product_rank_daily date={}, deletedRows={}", date, del);
					deleted = true;
				}

				List<MapSqlParameterSource> batch = new ArrayList<>(chunk.size());
				for (ProductScoreRow row : chunk.getItems()) {
					batch.add(new MapSqlParameterSource()
						.addValue("date", sqlDate)
						.addValue("productId", row.productId())
						.addValue("rankNo", nextRank++)
						.addValue("score", row.score()));
				}
				jdbc.batchUpdate(INSERT_SQL, batch.toArray(MapSqlParameterSource[]::new));
				log.info("inserted {} rows into product_rank_daily for date={}", chunk.size(), date);
			}
		};
	}
}
