package com.loopers.domain.order;

import com.loopers.domain.BaseTimeEntity;

import jakarta.persistence.Column;
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
@Table(name = "Orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

	@Column(name = "user_id")
	private Long userId;

	private OrderNumber orderNumber;

	private TotalOrderPrice totalOrderPrice;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Builder
	public Order(Long userId, OrderNumber orderNumber, TotalOrderPrice totalOrderPrice, OrderStatus status) {
		this.userId = userId;
		this.orderNumber = orderNumber;
		this.totalOrderPrice = totalOrderPrice;
		this.status = status;
	}
}
