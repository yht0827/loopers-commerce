package com.loopers.domain.product.entity;

import com.loopers.domain.BaseTimeEntity;
import com.loopers.domain.product.entity.vo.Quantity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "stocks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseTimeEntity {

	@Embedded
	private Quantity quantity;

	@Builder
	public Stock(Quantity quantity) {
		this.quantity = quantity;
	}
}
