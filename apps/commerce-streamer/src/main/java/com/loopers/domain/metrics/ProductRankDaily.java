package com.loopers.domain.metrics;

import java.time.LocalDate;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_rank_daily")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRankDaily extends BaseEntity {

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "rank_date", nullable = false)
	private LocalDate date;

	@Column(name = "score", nullable = false)
	private Double score;

	@Column(name = "rank_no", nullable = false)
	private Long rankNo;

	public static ProductRankDaily create(Long productId, LocalDate date, Double score, Long rankNo) {
		ProductRankDaily metrics = new ProductRankDaily();
		metrics.productId = productId;
		metrics.date = date;
		metrics.score = score;
		metrics.rankNo = rankNo;
		return metrics;
	}

}
