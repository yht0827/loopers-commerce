package com.loopers.domain.order.entity.vo;

import com.loopers.domain.order.entity.Delivery;

import jakarta.persistence.Embeddable;

@Embeddable
public record DeliveryVO(Long id, Long orderId, String trackingNumber, String courier, String status, String shippingAddress) {
	public static DeliveryVO from(Delivery delivery) {
		return new DeliveryVO(
			delivery.getId(),
			delivery.getOrderId().orderId(),
			delivery.getTrackingInfo().trackingNumber(),
			delivery.getTrackingInfo().courier(),
			delivery.getStatus().name(),
			delivery.getShippingAddress().shippingAddress()
		);
	}
}
