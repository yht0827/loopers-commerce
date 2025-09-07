package com.loopers.domain.order;

import java.io.Serializable;
import java.util.List;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TotalOrderPrice implements Serializable {
	
	@Column(name = "total_price")
	private Long totalPrice;
	
	public TotalOrderPrice(Long totalPrice) {
		if (totalPrice == null || totalPrice < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "총 주문 금액은 0 이상이어야 합니다.");
		}
		this.totalPrice = totalPrice;
	}

	public static TotalOrderPrice of(List<OrderItem> items) {
		long totalPrice = items.stream()
			.mapToLong(item -> item.getPrice().getPrice() * item.getQuantity().getQuantity())
			.sum();
		return new TotalOrderPrice(totalPrice);
	}
}


