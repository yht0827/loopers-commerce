package com.loopers.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_metrics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductMetrics extends BaseEntity {

	private Long productId;
	private Long viewCount = 0L;
	private Long likeCount = 0L;
	private Long salesCount = 0L;

	@Builder
	public ProductMetrics(Long productId, Long viewCount, Long likeCount, Long salesCount) {
		this.productId = productId;
		this.viewCount = viewCount;
		this.likeCount = likeCount;
		this.salesCount = salesCount;
	}

	public static ProductMetrics of(Long productId) {
		ProductMetrics metrics = new ProductMetrics();
		metrics.productId = productId;
		return metrics;
	}

	public void updateLikeCount(Long count) {
		this.likeCount = count;
	}
}
