package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentData;

public record PgClientDto() {

	public record PgPaymentRequest(
		String userId,
		String orderId,
		CardType cardType,
		String cardNo,
		Long amount,
		String callbackUrl
	) {

		public static PgPaymentRequest from(PaymentData.PaymentRequest request) {
			return new PgPaymentRequest(
				request.userId(),
				request.orderId(),
				request.cardType(),
				request.cardNo(),
				request.amount(),
				request.callbackUrl()
			);
		}
	}

	public record PgPaymentOrder(
		String orderId,
		Long amount,
		String cardNo,
		String cardType,
		String callbackUrl
	) {

	}

	public record PgPaymentTransaction(
		String transactionKey,
		String orderId,
		Long amount,
		String cardNo,
		String cardType,
		String status,
		String reason,
		String callbackUrl
	) {
	}
}
