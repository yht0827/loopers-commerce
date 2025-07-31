package com.loopers.domain.order.entity;

import java.time.ZonedDateTime;

import com.loopers.domain.BaseTimeEntity;
import com.loopers.domain.order.entity.vo.OrderNumber;
import com.loopers.domain.order.entity.vo.OrderPrice;
import com.loopers.domain.order.entity.vo.OrderStatus;

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
@Table(name = "Orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

	@Column(name = "user_id")
	private Long userId;

	@Embedded
	private OrderNumber orderNumber;

	@Embedded
	private OrderPrice totalPrice;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	private ZonedDateTime deletedAt;

	@Builder
	public Order(Long userId, OrderNumber orderNumber, OrderPrice totalPrice, OrderStatus status) {
		this.userId = userId;
		this.orderNumber = orderNumber;
		this.totalPrice = totalPrice;
		this.status = status;
	}
}
