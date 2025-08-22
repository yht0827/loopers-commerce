package com.loopers.infrastructure.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PgClient;
import com.loopers.domain.payment.TransactionStatus;
import com.loopers.interfaces.api.ApiResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PgClientImpl implements PgClient {

	private final PgFeignClient pgFeignClient;

	@CircuitBreaker(name = "pgClient", fallbackMethod = "requestFallback")
	@Retry(name = "pgClient")
	@Override
	public PaymentInfo.transaction request(PgClientDto.PgPaymentRequest request) {
		ApiResponse<PgClientDto.PgPaymentTransaction> response = pgFeignClient.request(request);
		PgClientDto.PgPaymentTransaction data = response.data();
		return PaymentInfo.transaction.toData(data);
	}

	public PaymentInfo.transaction requestFallback(PgClientDto.PgPaymentRequest request, Exception e) {
		log.warn("PG 결제 요청 실패, fallback 실행. orderId: {}, error: {}", request.orderId(), e.getMessage());
		return new PaymentInfo.transaction(
			null,
			request.orderId(),
			request.amount(),
			request.cardNo(),
			request.cardType(),
			TransactionStatus.FAILED,
			"PG 통신 실패",
			request.callbackUrl()
		);
	}

	@Override
	public PaymentInfo.order findOrder(PgClientDto.PgPaymentOrder request) {
		return null;
	}

	@Override
	public PaymentInfo.transaction findTransaction(PgClientDto.PgPaymentTransaction request) {
		return null;
	}

}
