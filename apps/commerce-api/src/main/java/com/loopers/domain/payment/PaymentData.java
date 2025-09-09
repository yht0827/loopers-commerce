package com.loopers.domain.payment;

import com.loopers.domain.order.OrderId;
import com.loopers.domain.common.Price;

public record PaymentData(
	String userId, String orderId, CardType cardType, String cardNo, Long amount,
	String callbackUrl
) {
	public record PaymentRequest(String userId, String orderId, CardType cardType, String cardNo, Long amount,
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

	}
}
