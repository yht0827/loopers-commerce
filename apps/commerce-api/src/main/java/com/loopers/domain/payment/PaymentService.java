package com.loopers.domain.payment;

import org.springframework.stereotype.Service;

import com.loopers.application.payment.PaymentCommand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentGatewayService paymentGatewayService;

	public void createPayment(final PaymentData.PaymentRequest data) {
		Payment payment = data.toEntity();
		paymentRepository.save(payment);
	}

	public PaymentInfo updatePaymentStatus(PaymentData.PaymentRequest data, PaymentInfo.transaction pgResponse) {
		Payment payment = data.toEntity();

		payment.updateTransactionKey(data.orderId());

		if (pgResponse.status().equals(TransactionStatus.SUCCESS)) {
			payment.processPaymentSuccess();
		}

		return PaymentInfo.from(payment);
	}

	public PaymentInfo processCallback(PaymentCommand.ProcessCallback command) {
		Payment payment = paymentRepository.findByTransactionKey(command.transactionKey())
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

		return paymentGatewayService.processCallbackWithVerification(payment, command);
	}
}
