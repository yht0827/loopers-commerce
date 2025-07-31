package com.loopers.domain.order.entity;

import com.loopers.domain.BaseTimeEntity;
import com.loopers.domain.order.entity.vo.Address;
import com.loopers.domain.order.entity.vo.DeliveryStatus;
import com.loopers.domain.order.entity.vo.OrderId;
import com.loopers.domain.order.entity.vo.TrackingInfo;

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
@Table(name = "deliveries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity {

	@Embedded
	private OrderId orderId;

	@Embedded
	private TrackingInfo trackingInfo;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

	@Embedded
	private Address shippingAddress;

	@Builder
	public Delivery(OrderId orderId, TrackingInfo trackingInfo, DeliveryStatus status, Address shippingAddress) {
		this.orderId = orderId;
		this.trackingInfo = trackingInfo;
		this.status = status;
		this.shippingAddress = shippingAddress;
	}
}
