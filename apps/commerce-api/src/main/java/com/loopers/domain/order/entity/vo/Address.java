package com.loopers.domain.order.entity.vo;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(String shippingAddress) implements Serializable {
	public Address {
		if (shippingAddress == null || shippingAddress.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "배송 주소는 비어있을 수 없습니다.");
		}
	}
}
