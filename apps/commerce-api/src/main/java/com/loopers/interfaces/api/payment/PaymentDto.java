package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCommand;
import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.TransactionStatus;

public record PaymentDto() {

	public record V1() {

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
				return switch (paymentResult) {
					case PaymentResult.Success success -> new CallbackResponse(
						success.transactionKey(),
						success.orderId(),
						success.paymentInfo().status(),
						"결제 콜백 처리 완료"
					);
					case PaymentResult.Failure failure -> new CallbackResponse(
						"FAILED_" + failure.orderId(),
						failure.orderId(),
						TransactionStatus.FAILED,
						"결제 콜백 처리 실패: " + failure.reason()
					);
				};
			}
		}
	}
}
