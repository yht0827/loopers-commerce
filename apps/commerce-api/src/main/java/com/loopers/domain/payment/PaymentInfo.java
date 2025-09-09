package com.loopers.domain.payment;

import java.util.List;

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
			payment.getTransactionKey().getTransactionKey(),
			payment.getOrderId().getOrderId(),
			payment.getCardType(),
			payment.getCardNo().getCardNo(),
			payment.getAmount().getPrice(),
			payment.getStatus(),
			payment.getReason().reason()
		);
	}

	public record transaction(
		String transactionKey,
		String orderId,
		Long amount,
		String cardNo,
		CardType cardType,
		TransactionStatus status,
		String reason
	) {

		public static PaymentInfo.transaction toData(PgClientDto.PgPaymentTransaction pgPaymentTransaction) {
			return new PaymentInfo.transaction(
				pgPaymentTransaction.transactionKey(),
				pgPaymentTransaction.orderId(),
				pgPaymentTransaction.amount(),
				pgPaymentTransaction.cardNo(),
				pgPaymentTransaction.cardType(),
				pgPaymentTransaction.status(),
				pgPaymentTransaction.reason()
			);
		}

	}

	public record order(
		String orderId,
		List<TransactionResponse> transactions
	) {
		public static PaymentInfo.order toData(PgClientDto.PgPaymentOrderResponse pgPaymentOrderResponse) {
			List<TransactionResponse> list = pgPaymentOrderResponse.transactions().stream()
				.map(transactionResponse -> new TransactionResponse(
					transactionResponse.transactionKey(),
					transactionResponse.status(),
					transactionResponse.reason()
				)).toList();

			return new PaymentInfo.order(
				pgPaymentOrderResponse.orderId(),
				list
			);
		}
	}

	public record TransactionResponse(
		String transactionKey,
		TransactionStatus status,
		String reason
	) {
	}

}
