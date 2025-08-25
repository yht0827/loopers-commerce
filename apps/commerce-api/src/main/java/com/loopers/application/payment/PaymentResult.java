package com.loopers.application.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.TransactionStatus;

public record PaymentResult(
	String transactionKey,
	String orderId,
	CardType cardType,
	String cardNo,
	Long amount,
	TransactionStatus status,
	String reason
) {

	public static PaymentResult from(PaymentInfo info) {
		return new PaymentResult(info.transactionKey(), info.orderId(), info.cardType(), info.cardNo(), info.amount(),
			info.status(), info.reason());
	}
}
