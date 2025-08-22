package com.loopers.infrastructure.payment;

import java.util.List;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentData;
import com.loopers.domain.payment.TransactionStatus;

public record PgClientDto() {

	public record PgPaymentRequest(String userId, String orderId, CardType cardType, String cardNo, Long amount,
								   String callbackUrl) {

		public static PgPaymentRequest from(PaymentData.PaymentRequest request) {
			return new PgPaymentRequest(request.userId(), request.orderId(), request.cardType(), request.cardNo(),
				request.amount(), request.callbackUrl());
		}
	}

	public record PgPaymentOrderResponse(
		String orderId,
		List<TransactionResponse> transactions
	) {
	}

	public record TransactionResponse(
		String transactionKey,
		TransactionStatus status,
		String reason
	) {
	}

	public record PgPaymentTransaction(
		String transactionKey,
		String orderId,
		Long amount,
		String cardNo,
		CardType cardType,
		TransactionStatus status,
		String reason
	) {
	}

}
