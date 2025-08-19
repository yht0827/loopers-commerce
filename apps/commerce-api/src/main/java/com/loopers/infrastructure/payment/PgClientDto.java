package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentData;
import com.loopers.domain.payment.TransactionStatus;

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

	public record PgPaymentResponse(
		String transactionKey,
		String orderId,
		CardType cardType,
		String cardNo,
		Long amount,
		TransactionStatus status,
		String reason
	) {

		public PaymentData.PaymentResponse toPaymentResponse() {
			return new PaymentData.PaymentResponse(
				transactionKey,
				orderId,
				cardType,
				cardNo,
				amount,
				status,
				reason
			);
		}
	}
}
