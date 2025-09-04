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

	private Quantity quantity;

	@Version
	private Long version;

	@Builder
	public Product(final BrandId brandId, final ProductName name, final Price price, final Quantity quantity) {
		this.brandId = brandId;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public void deduct(final Quantity amount) {
		this.quantity = this.quantity.subtractWithValidation(amount);
	}
}
