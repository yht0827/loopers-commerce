package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCommand;
import com.loopers.application.payment.PaymentInfo;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.TransactionStatus;

public record PaymentDto() {

	public record V1() {

		public record PaymentRequest(String userId, Long orderId, CardTypeDto cardType, String cardNo, Long amount,
									 String callbackUrl) {

			public PaymentCommand.CreatePayment toCriteria(final String userId) {
				return new PaymentCommand.CreatePayment(userId, orderId, cardType.toCardType(), cardNo, amount, callbackUrl);
			}
		}

		public record PaymentResponse(String transactionKey, Long orderId, CardType cardType, String cardNo, Long amount,
									  TransactionStatus status, String reason) {

			public static PaymentResponse from(PaymentInfo paymentInfo) {
				return new PaymentResponse(paymentInfo.transactionKey(), paymentInfo.orderId(), paymentInfo.cardType(),
					paymentInfo.cardNo(), paymentInfo.amount(), paymentInfo.status(), paymentInfo.reason());
			}
		}
	}

	public enum CardTypeDto {
		SAMSUNG, KB, HYUNDAI;

		public CardType toCardType() {
			return switch (this) {
				case SAMSUNG -> CardType.SAMSUNG;
				case KB -> CardType.KB;
				case HYUNDAI -> CardType.HYUNDAI;
			};
		}

		public static CardTypeDto from(CardType cardType) {
			return switch (cardType) {
				case SAMSUNG -> SAMSUNG;
				case KB -> KB;
				case HYUNDAI -> HYUNDAI;
			};
		}
	}

}
