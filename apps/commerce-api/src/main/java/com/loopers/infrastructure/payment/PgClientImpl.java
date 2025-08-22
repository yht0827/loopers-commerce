package com.loopers.infrastructure.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PgClient;
import com.loopers.interfaces.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PgClientImpl implements PgClient {

	private final PgFeignClient pgFeignClient;

	@Override
	public PaymentInfo.transaction request(PgClientDto.PgPaymentRequest request) {
		ApiResponse<PgClientDto.PgPaymentTransaction> response = pgFeignClient.request(request);

		PgClientDto.PgPaymentTransaction data = response.data();
		return PaymentInfo.transaction.toData(data);
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
