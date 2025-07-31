package com.loopers.domain.order.entity;

import com.loopers.domain.BaseTimeEntity;
import com.loopers.domain.order.entity.vo.OrderId;
import com.loopers.domain.order.entity.vo.OrderNumber;
import com.loopers.domain.order.entity.vo.PaymentDetail;
import com.loopers.domain.order.entity.vo.PaymentStatus;

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
@Table(name = "order_status_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {

	@Embedded
	private OrderId orderId;

	@Embedded
	private OrderNumber orderNumber;

	@Embedded
	private PaymentDetail detail;

	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	@Builder
	public Payment(OrderId orderId, OrderNumber orderNumber, PaymentDetail detail, PaymentStatus status) {
		this.orderId = orderId;
		this.orderNumber = orderNumber;
		this.detail = detail;
		this.status = status;
	}
}
