package com.loopers.domain.order.entity;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.order.entity.vo.DeliveryItemId;
import com.loopers.domain.order.entity.vo.ItemQuantity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "delivery_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryItem extends BaseEntity {

	@Embedded
	private DeliveryItemId id;

	@Embedded
	private ItemQuantity quantity;

	@Builder
	public DeliveryItem(DeliveryItemId id, ItemQuantity quantity) {
		this.id = id;
		this.quantity = quantity;
	}
}
