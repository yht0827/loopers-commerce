package com.loopers.domain.product;

import com.loopers.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

	@Column(name = "brand_id")
	private Long brandId;

	private ProductName name;

	private Price price;

	private LikeCount likeCount;

	private Quantity quantity;

	@Builder
	public Product(Long brandId, ProductName name, Price price, LikeCount likeCount, Quantity quantity) {
		this.brandId = brandId;
		this.name = name;
		this.price = price;
		this.likeCount = likeCount;
		this.quantity = quantity;
	}

	public void updateLikeCount(LikeCount likeCount) {
		this.likeCount = likeCount;
	}

	public void decreaseStock(long amount) {
		this.quantity = this.quantity.subtract(amount);
	}
}
