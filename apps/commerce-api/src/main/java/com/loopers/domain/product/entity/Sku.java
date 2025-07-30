package com.loopers.domain.product.entity;

import com.loopers.domain.BaseTimeEntity;
import com.loopers.domain.product.entity.vo.Price;
import com.loopers.domain.product.entity.vo.SkuCode;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "skus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sku extends BaseTimeEntity {

	@Column(name = "product_id")
	private Long productId;

	@Embedded
	private SkuCode skuCode;

	@Embedded
	private Price price;

	@Builder
	public Sku(Long productId, SkuCode skuCode, Price price) {
		this.productId = productId;
		this.skuCode = skuCode;
		this.price = price;
	}
}
