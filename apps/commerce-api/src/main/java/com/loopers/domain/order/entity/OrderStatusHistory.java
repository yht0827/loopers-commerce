package com.loopers.domain.order.entity;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.order.entity.vo.OrderId;
import com.loopers.domain.order.entity.vo.OrderStatus;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_status_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatusHistory extends BaseEntity {

	@Embedded
	private OrderId orderId;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@CreatedDate
	private ZonedDateTime createdAt;

	public OrderStatusHistory(OrderId orderId, OrderStatus status) {
		this.orderId = orderId;
		this.status = status;
	}
}
