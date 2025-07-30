package com.loopers.domain.product.entity;

import com.loopers.domain.BaseTimeEntity;
import com.loopers.domain.product.entity.vo.ProductName;

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
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

	@Column(name = "brand_id")
	private Long brandId;

	@Embedded
	private ProductName name;

	@Builder
	public Product(Long brandId, ProductName name) {
		this.brandId = brandId;
		this.name = name;
	}
}
