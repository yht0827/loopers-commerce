package com.loopers.domain.order.entity;

import com.loopers.domain.BaseTimeEntity;
import com.loopers.domain.order.entity.vo.OrderItemDetail;
import com.loopers.domain.order.entity.vo.OrderItemIdentifier;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_Items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseTimeEntity {

	@Embedded
	private OrderItemIdentifier identifier;

	@Embedded
	private OrderItemDetail detail;

	@Builder
	public OrderItem(OrderItemIdentifier identifier, OrderItemDetail detail) {
		this.identifier = identifier;
		this.detail = detail;
	}
}
