package com.loopers.domain.order.entity.vo;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record TrackingInfo(String trackingNumber, String courier) implements Serializable {
	public TrackingInfo {
		if (trackingNumber == null || trackingNumber.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "운송장 번호는 비어있을 수 없습니다.");
		}
		if (courier == null || courier.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "배송업체는 비어있을 수 없습니다.");
		}
	}
}
