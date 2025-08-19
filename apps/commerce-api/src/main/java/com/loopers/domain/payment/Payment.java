package com.loopers.domain.payment;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.common.OrderId;
import com.loopers.domain.common.Price;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

	OrderId orderId;

	@Enumerated(EnumType.STRING)
	CardType cardType;

	CardNo cardNo;

	Price amount;

	CallbackUrl callbackUrl;

	TransactionKey transactionKey;

	TransactionStatus status;

	PaymentReason reason;

	@Builder
	public Payment(OrderId orderId, CardType cardType, CardNo cardNo, Price amount, CallbackUrl callbackUrl,
		TransactionKey transactionKey, TransactionStatus status, PaymentReason reason) {
		this.orderId = orderId;
		this.cardType = cardType;
		this.cardNo = cardNo;
		this.amount = amount;
		this.callbackUrl = callbackUrl;
		this.transactionKey = transactionKey;
		this.status = status;
		this.reason = reason;
	}

	public void updateStatus() {
		this.status = TransactionStatus.SUCCESS;
	}

}
