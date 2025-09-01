package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.common.ProductId;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "product_aggregate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAggregate extends BaseEntity {

	private ProductId productId;

	private LikeCount likeCount;

	@Builder
	public ProductAggregate(ProductId productId, LikeCount likeCount) {
		this.productId = productId;
		this.likeCount = likeCount;
	}

	public void updateLikeCount(final LikeCount likeCount) {
		this.likeCount = likeCount;
	}

}
