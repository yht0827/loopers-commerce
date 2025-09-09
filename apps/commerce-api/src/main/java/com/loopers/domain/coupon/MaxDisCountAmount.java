package com.loopers.domain.coupon;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MaxDisCountAmount implements Serializable {

    @Column(name = "max_discount_amount")
    private Long maxDisCountAmount;
    
    public MaxDisCountAmount(Long maxDisCountAmount) {
        if (maxDisCountAmount == null || maxDisCountAmount < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "최대 할인 금액은 0 이상이어야 합니다.");
        }
        this.maxDisCountAmount = maxDisCountAmount;
    }

    public Long applyMaxDiscountLimit(Long calculatedDiscount) {
        return Math.min(calculatedDiscount, maxDisCountAmount);
    }

    public boolean hasLimit() {
        return maxDisCountAmount != null;
    }
}
