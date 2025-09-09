package com.loopers.domain.payment;

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
public class TransactionKey implements Serializable {

	@Column(name = "transaction_key")
	private String transactionKey;

	public TransactionKey(String transactionKey) {
		if (transactionKey == null || transactionKey.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "트랜잭션 키는 비어있을 수 없습니다.");
		}
		this.transactionKey = transactionKey;
	}
}
