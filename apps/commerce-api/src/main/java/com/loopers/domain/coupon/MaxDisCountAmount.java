package com.loopers.domain.coupon;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record MaxDisCountAmount(Long maxDisCountAmount) implements Serializable {

    public MaxDisCountAmount {
        if (maxDisCountAmount == null || maxDisCountAmount < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "최대 할인 금액은 0 이상이어야 합니다.");
        }
    }
}
