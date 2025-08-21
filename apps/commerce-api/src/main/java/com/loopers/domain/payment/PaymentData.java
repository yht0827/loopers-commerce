package com.loopers.domain.payment;

import com.loopers.application.payment.PaymentInfo;
import com.loopers.domain.common.OrderId;
import com.loopers.domain.common.PaymentId;
import com.loopers.domain.common.Price;
import com.loopers.infrastructure.payment.PgClientDto;

public record PaymentData() {

	public record PaymentRequest(String userId, Long orderId, CardType cardType, String cardNo, Long amount,
								 String callbackUrl) {

		public Payment toEntity() {
			return Payment.builder()
				.orderId(new OrderId(orderId))
				.cardType(cardType)
				.cardNo(new CardNo(cardNo))
				.amount(new Price(amount))
				.status(TransactionStatus.PENDING)
				.reason(new PaymentReason("결제 요청"))
				.callbackUrl(new CallbackUrl(callbackUrl))
				.build();
		}

		public PaymentHistory toHistory(final Payment payment, final PgClientDto.PgPaymentResponse pgPaymentResponse) {
			return PaymentHistory.builder()
				.paymentId(new PaymentId(payment.getId()))
				.oldStatus(payment.getStatus())
				.newStatus(pgPaymentResponse.status())
				.reason(new PaymentReason("결제 요청"))
				.build();
		}

	}

	public record PaymentResponse(String transactionKey, Long orderId, CardType cardType, String cardNo, Long amount,
								  TransactionStatus status, String reason) {
		public PaymentInfo toInfo() {
			return new PaymentInfo(transactionKey, orderId, cardType, cardNo, amount, status, reason);
		}

	}
}
