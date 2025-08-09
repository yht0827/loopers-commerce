package com.loopers.domain.coupon;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record DiscountValue(Long discountValue) implements Serializable {
    public DiscountValue {
        if (discountValue == null || discountValue < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "할인 금액은 0 이상이어야 합니다.");
        }
    }
}
