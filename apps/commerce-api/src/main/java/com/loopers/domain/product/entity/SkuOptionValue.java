package com.loopers.domain.product.entity;

import com.loopers.domain.product.entity.vo.SkuOptionValueId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "sku_option_values")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SkuOptionValue {
	@EmbeddedId
	private SkuOptionValueId id;
}
