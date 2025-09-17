package com.loopers.domain.metrics;

import java.time.LocalDate;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "product_metrics_daily")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductMetricsDaily extends BaseEntity {

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "date", nullable = false)
	private LocalDate date;

	@Column(name = "view_count", nullable = false)
	private Long viewCount;

	@Column(name = "like_count", nullable = false)
	private Long likeCount;

	@Column(name = "order_count", nullable = false)
	private Long orderCount;

	@Column(name = "score", nullable = false)
	private Double score;

	public static ProductMetricsDaily create(Long productId, LocalDate date) {
		ProductMetricsDaily metrics = new ProductMetricsDaily();
		metrics.productId = productId;
		metrics.date = date;
		return metrics;
	}

	public void addLike(long delta) {
		this.likeCount += delta;
	}

	public void addScore(double d) {
		this.score += d;
	}

	public void addView(long delta) {
		this.viewCount += delta;
	}

	public void addOrder(long delta) {
		this.orderCount += delta;
	}

}
