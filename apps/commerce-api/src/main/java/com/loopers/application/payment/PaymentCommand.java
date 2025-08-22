package com.loopers.application.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentData;
import com.loopers.domain.payment.TransactionStatus;

public record PaymentCommand() {

	public record CreatePayment(
		String userId,
		String orderId,
		CardType cardType,
		String cardNo,
		Long amount,
		String callbackUrl
	) {
		public PaymentData.PaymentRequest toData() {
			return new PaymentData.PaymentRequest(userId, orderId, cardType, cardNo, amount, callbackUrl);
		}
	}

	public record ProcessCallback(
		String transactionKey,
		TransactionStatus status,
		String orderId
	) {
	}
}
