package com.loopers.domain.product.entity;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.product.entity.vo.QuantityChange;
import com.loopers.domain.product.entity.vo.StockHistoryReason;
import com.loopers.domain.product.entity.vo.StockHistoryType;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "stock_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockHistory extends BaseEntity {

	@Column(name = "sku_id")
	private Long skuId;

	@Enumerated(EnumType.STRING)
	private StockHistoryType type;

	@Embedded
	private QuantityChange quantityChange;

	@Embedded
	private StockHistoryReason reason;

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private ZonedDateTime createdAt;

	@Builder
	public StockHistory(Long skuId, StockHistoryType type, QuantityChange quantityChange, StockHistoryReason reason) {
		this.skuId = skuId;
		this.type = type;
		this.quantityChange = quantityChange;
		this.reason = reason;
	}
}
