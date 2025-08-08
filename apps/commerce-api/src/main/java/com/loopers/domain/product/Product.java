package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.common.BrandId;
import com.loopers.domain.common.Price;
import com.loopers.domain.common.Quantity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

	private BrandId brandId;

	private ProductName name;

	private Price price;

	private LikeCount likeCount;

	private Quantity quantity;

	@Version
	private Long version;

	@Builder
	public Product(BrandId brandId, ProductName name, Price price, LikeCount likeCount, Quantity quantity) {
		this.brandId = brandId;
		this.name = name;
		this.price = price;
		this.likeCount = likeCount;
		this.quantity = quantity;
	}

	public void updateLikeCount(LikeCount likeCount) {
		this.likeCount = likeCount;
	}

	public void decreaseStock(Long amount) {
		this. quantity = quantity.subtract(amount);
	}
}
