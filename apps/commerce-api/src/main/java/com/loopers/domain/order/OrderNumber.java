package com.loopers.domain.order;

import java.io.Serializable;

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
public class OrderNumber implements Serializable {
	
	@Column(name = "order_number")
	private String orderNumber;
	
	public OrderNumber(String orderNumber) {
		if (orderNumber == null || orderNumber.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 번호는 비어있을 수 없습니다.");
		}
		this.orderNumber = orderNumber;
	}
}
