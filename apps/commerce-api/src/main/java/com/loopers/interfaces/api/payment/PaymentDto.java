package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCommand;
import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.TransactionStatus;

public record PaymentDto() {

	public record V1() {

		public record PaymentRequest(String userId, String orderId, CardTypeDto cardType, String cardNo, Long amount,
									 String callbackUrl) {

			public PaymentCommand.CreatePayment toCriteria(final String userId) {
				return new PaymentCommand.CreatePayment(userId, orderId, cardType.toCardType(), cardNo, amount, callbackUrl);
			}
		}

		public record PaymentResponse(String transactionKey, String orderId, CardType cardType, String cardNo, Long amount,
									  TransactionStatus status, String reason) {

			public static PaymentResponse from(PaymentResult paymentResult) {
				return new PaymentResponse(paymentResult.transactionKey(), paymentResult.orderId(), paymentResult.cardType(),
					paymentResult.cardNo(), paymentResult.amount(), paymentResult.status(), paymentResult.reason());
			}
		}

		public record CallbackRequest(
			String transactionKey,
			String orderId,
			CardType cardType,
			String cardNo,
			Long amount,
			TransactionStatus status,
			String reason) {

			public PaymentCommand.ProcessCallback toCommand() {
				return new PaymentCommand.ProcessCallback(transactionKey, status, orderId);
			}
		}

		public record CallbackResponse(String transactionKey, String orderId, TransactionStatus status, String message) {

			public static CallbackResponse from(PaymentResult paymentResult) {
				return new CallbackResponse(
					paymentResult.transactionKey(),
					paymentResult.orderId(),
					paymentResult.status(),
					"결제 콜백 처리 완료"
				);
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
