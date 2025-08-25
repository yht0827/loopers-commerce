package com.loopers.domain.order;

import java.io.Serializable;
import java.util.List;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record TotalOrderPrice(Long totalPrice) implements Serializable {
	public TotalOrderPrice {
		if (totalPrice == null || totalPrice < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "총 주문 금액은 0 이상이어야 합니다.");
		}
	}

	public static TotalOrderPrice of(List<OrderItem> items) {
		long totalPrice = items.stream()
			.mapToLong(item -> item.getPrice().price() * item.getQuantity().quantity())
			.sum();
		return new TotalOrderPrice(totalPrice);
	}

}


