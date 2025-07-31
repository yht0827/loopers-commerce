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
@Table(name = "order_status_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {

	private OrderId orderId;

	@Column(name = "user_id")
	private Long userId;

	private PaymentDetail detail;

	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	@Builder
	public Payment(OrderId orderId, Long userId, PaymentDetail detail, PaymentStatus status) {
		this.orderId = orderId;
		this.userId = userId;
		this.detail = detail;
		this.status = status;
	}
}
