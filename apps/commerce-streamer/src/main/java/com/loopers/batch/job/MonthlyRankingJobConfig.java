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
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.loopers.batch.dto.ProductScoreRow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MonthlyRankingJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;
	private final NamedParameterJdbcTemplate jdbc;

	private static final String JOB_NAME = "monthlyRankingJob";
	private static final String STEP_NAME = "monthlyRankingStep";
	private static final String READER_NAME = "monthlyReader";
	private static final int CHUNK_SIZE = 100;
	private static final int DEFAULT_TOP_N = 100;
	private static final String SELECT_MONTHLY_SQL = """
		SELECT product_id,
			SUM(score) AS total_score,
			SUM(like_count) AS total_like,
			SUM(view_count) AS total_view,
			SUM(order_count) AS total_order
		FROM product_metrics_daily
		WHERE snapshot_date BETWEEN ? AND ?
		GROUP BY product_id
		ORDER BY total_score DESC, product_id DESC
		LIMIT ?
		""";
	private static final String DELETE_SQL =
		"DELETE FROM product_metrics_monthly WHERE period_key = :periodKey";
	private static final String INSERT_SQL = """
		INSERT INTO product_metrics_monthly (period_key, product_id, rank_no, score, like_count, view_count, order_count, created_at, updated_at)
		VALUES (:periodKey, :productId, :ranking, :score, :likeCount, :viewCount, :orderCount, NOW(), NOW())
		""";

	@Bean
	public Job monthlyRankingJob(Step monthlyRankingStep) {
		log.info("MonthlyRankingJob 배치 등록 완료");
		return new JobBuilder(JOB_NAME, jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(monthlyRankingStep)
			.build();
	}

	@Bean
	public Step monthlyRankingStep(
		ItemReader<ProductScoreRow> monthlyReader,
		ItemProcessor<ProductScoreRow, ProductScoreRow> monthlyProcessor,
		ItemWriter<ProductScoreRow> monthlyWriter
	) {
		log.info("월간 랭킹 step 구성 완료 - stepName={}, chunkSize={}", STEP_NAME, CHUNK_SIZE);
		return new StepBuilder(STEP_NAME, jobRepository)
			.<ProductScoreRow, ProductScoreRow>chunk(CHUNK_SIZE, transactionManager)
			.reader(monthlyReader)
			.processor(monthlyProcessor)
			.writer(monthlyWriter)
			.faultTolerant()
			.build();
	}

	@Bean
	@StepScope
	public ItemReader<ProductScoreRow> monthlyReader(
		@Value("#{jobParameters['startDate']}") String startDateParam,
		@Value("#{jobParameters['endDate']}") String endDateParam,
		@Value("#{jobParameters['topN']}") Integer topN
	) {
		LocalDate startDate = LocalDate.parse(startDateParam);
		LocalDate endDate = LocalDate.parse(endDateParam);

		log.info("월간 랭킹 reader 구성 완료 - startDate={}, endDate={}, topN={} (기본 {})",
			startDate, endDate, topN, DEFAULT_TOP_N);

		return new JdbcCursorItemReaderBuilder<ProductScoreRow>()
			.name(READER_NAME)
			.dataSource(dataSource)
			.sql(SELECT_MONTHLY_SQL)
			.preparedStatementSetter(ps -> {
				ps.setDate(1, Date.valueOf(startDate));
				ps.setDate(2, Date.valueOf(endDate));
				ps.setInt(3, topN);
			})
			.rowMapper((rs, rowNum) -> new ProductScoreRow(
				rs.getLong("product_id"),
				rs.getDouble("total_score"),
				rs.getLong("total_like"),
				rs.getLong("total_view"),
				rs.getLong("total_order")))
			.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<ProductScoreRow, ProductScoreRow> monthlyProcessor() {
		return item -> item;
	}

	@Bean
	@StepScope
	public ItemWriter<ProductScoreRow> monthlyWriter(
		@Value("#{jobParameters['periodKey']}") String periodKey,
		@Value("#{jobParameters['dryRun']}") boolean dryRun
	) {
		return new ItemWriter<>() {
			private boolean deleted = false;
			private long nextRank = 1L;

			@Override
			public void write(Chunk<? extends ProductScoreRow> chunk) {
				if (chunk == null || chunk.isEmpty()) {
					return;
				}

				if (dryRun) {
					log.info("[DRY-RUN] 월간 랭킹 저장 예정 - periodKey={}, 건수={}, 순위범위={}..{}",
						periodKey, chunk.size(), nextRank, nextRank + chunk.size() - 1);
					nextRank += chunk.size();
					return;
				}

				if (!deleted) {
					int deletedCount = jdbc.update(DELETE_SQL,
						new MapSqlParameterSource().addValue("periodKey", periodKey));
					log.info("product_metrics_monthly 선 삭제 완료 - periodKey={}, 삭제행수={}", periodKey, deletedCount);
					deleted = true;
				}

				List<MapSqlParameterSource> batch = new ArrayList<>(chunk.size());
				for (ProductScoreRow row : chunk.getItems()) {
					batch.add(new MapSqlParameterSource()
						.addValue("periodKey", periodKey)
						.addValue("productId", row.productId())
						.addValue("ranking", nextRank++)
						.addValue("score", row.score())
						.addValue("likeCount", row.likeCount())
						.addValue("viewCount", row.viewCount())
						.addValue("orderCount", row.orderCount()));
				}
				jdbc.batchUpdate(INSERT_SQL, batch.toArray(MapSqlParameterSource[]::new));
				log.info("product_metrics_monthly 적재 완료 - periodKey={}, 저장행수={}", periodKey, chunk.size());
			}
		};
	}
}
