package com.loopers.domain.point;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import java.math.BigDecimal;

import com.loopers.support.error.CoreException;

import jakarta.persistence.Embeddable;

@Embeddable
public record Balance(BigDecimal balance) {

	public Balance {
		if (balance == null) {
			throw new CoreException(BAD_REQUEST, POINT_BALANCE_REQUIRED.getMessage());
		}
		if (balance.compareTo(BigDecimal.ZERO) < 0) {
			throw new CoreException(BAD_REQUEST, POINT_BALANCE_INVALID.format(0));
		}
	}

	public Balance charge(final Balance amount) {
		return new Balance(this.balance.add(amount.balance()));
	}

	public static Balance of(final BigDecimal amount) {
		return new Balance(amount);
	}
}
