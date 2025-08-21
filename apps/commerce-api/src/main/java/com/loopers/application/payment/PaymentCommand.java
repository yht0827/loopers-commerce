package com.loopers.application.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentData;

public record PaymentCommand() {

	public record CreatePayment(
		String userId,
		Long orderId,
		CardType cardType,
		String cardNo,
		Long amount,
		String callbackUrl
	) {
		public PaymentData.PaymentRequest toData() {
			return new PaymentData.PaymentRequest(userId, orderId, cardType, cardNo, amount, callbackUrl);
		}

	}
}
