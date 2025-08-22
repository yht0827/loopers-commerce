package com.loopers.domain.payment;

import org.springframework.stereotype.Service;

import com.loopers.infrastructure.payment.PgClientDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentGatewayService {
	private final PgClient pgClient;

	public PaymentInfo.transaction requestPaymentGateWay(final PaymentData.PaymentRequest data) {
		PgClientDto.PgPaymentRequest pgRequest = PgClientDto.PgPaymentRequest.from(data);
		return pgClient.request(pgRequest);
	}
}
