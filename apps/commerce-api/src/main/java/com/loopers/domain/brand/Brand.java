package com.loopers.domain.brand;

import com.loopers.domain.BaseTimeEntity;

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
public class Brand extends BaseTimeEntity {

	private BrandName name;

	@Builder
	public Brand(BrandName name) {
		this.name = name;
	}
}
