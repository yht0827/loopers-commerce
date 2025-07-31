package com.loopers.domain.order.entity.vo;

import com.loopers.domain.order.entity.DeliveryItem;

import jakarta.persistence.Embeddable;

@Embeddable
public record DeliveryItemVO(Long delivery_id, Long order_item_id, Long quantity) {
	public static DeliveryItemVO from(DeliveryItem deliveryItem) {
		return new DeliveryItemVO(
			deliveryItem.getId().deliveryId(),
			deliveryItem.getId().orderItemId(),
			deliveryItem.getQuantity().quantity()
		);
	}
}
