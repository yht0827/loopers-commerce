package com.loopers.domain.payment;

import org.springframework.stereotype.Service;

import com.loopers.application.payment.PaymentInfo;
import com.loopers.infrastructure.payment.PgClient;
import com.loopers.infrastructure.payment.PgClientDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PgClient pgClient;
	private final PaymentRepository paymentRepository;
	private final PaymentHistoryRepository paymentHistoryRepository;

	public PaymentInfo processPayment(PaymentData.PaymentRequest paymentRequest) {

		// 결제 엔티티 생성 및 저장
		Payment payment = paymentRequest.toEntity();
		paymentRepository.save(payment);

		// PG 결제 처리
		PgClientDto.PgPaymentRequest pgRequest = PgClientDto.PgPaymentRequest.from(paymentRequest);
		PgClientDto.PgPaymentResponse pgPaymentResponse = pgClient.requestPayment(pgRequest);

		// 결제 상태 업데이트


		// 상태 변경 이력 추가
		PaymentHistory paymentHistory = paymentRequest.toHistory(payment, pgPaymentResponse);
		paymentHistoryRepository.save(paymentHistory);

		// 결과 반환
		return pgPaymentResponse.toPaymentResponse().toInfo();
	}
}
