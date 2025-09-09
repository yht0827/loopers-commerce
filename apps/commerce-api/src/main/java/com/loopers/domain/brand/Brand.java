package com.loopers.domain.brand;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.AttributeOverride;
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
@Table(name = "brands")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseEntity {

	@Embedded
	@AttributeOverride(name = "brandName", column = @Column(name = "brand_name"))
	private BrandName brandName;

	@Builder
	public Brand(BrandName brandName) {
		this.brandName = brandName;
	}
}
