package com.loopers.domain.payment;

import com.loopers.infrastructure.payment.PgClientDto;

public record PaymentInfo(
	String transactionKey,
	String orderId,
	CardType cardType,
	String cardNo,
	Long amount,
	TransactionStatus status,
	String reason
) {

	public static PaymentInfo from(final Payment payment) {
		return new PaymentInfo(
			payment.getTransactionKey().transactionKey(),
			payment.getOrderId().orderId(),
			payment.getCardType(),
			payment.getCardNo().cardNo(),
			payment.getAmount().price(),
			payment.getStatus(),
			payment.getReason().reason()
		);
	}

	public record transaction(
		String orderId,
		Long amount,
		String cardNo,
		String cardType,
		String callbackUrl
	) {

		public static PaymentInfo.transaction toData(PgClientDto.PgPaymentTransaction pgPaymentTransaction) {
			return new PaymentInfo.transaction(
				pgPaymentTransaction.orderId(),
				pgPaymentTransaction.amount(),
				pgPaymentTransaction.cardNo(),
				pgPaymentTransaction.cardType(),
				pgPaymentTransaction.callbackUrl());
		}

	}

	public record order(
		String orderId,
		Long amount,
		String cardNo,
		String cardType,
		String callbackUrl
	) {
		public static PaymentInfo.order toData(PgClientDto.PgPaymentOrder pgPaymentOrder) {
			return new PaymentInfo.order(
				pgPaymentOrder.orderId(),
				pgPaymentOrder.amount(),
				pgPaymentOrder.cardNo(),
				pgPaymentOrder.cardType(),
				pgPaymentOrder.callbackUrl()
			);
		}

	}

}
