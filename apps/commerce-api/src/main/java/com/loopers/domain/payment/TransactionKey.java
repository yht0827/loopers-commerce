package com.loopers.domain.payment;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record TransactionKey(String transactionKey) implements Serializable {

	public TransactionKey {
		if (transactionKey == null || transactionKey.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "트랜잭션 키는 비어있을 수 없습니다.");
		}
	}
}
