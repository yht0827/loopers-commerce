package com.loopers.domain.payment;

import org.springframework.web.bind.annotation.RequestBody;

import com.loopers.infrastructure.payment.PgClientDto;

public interface PgClient {

	PaymentInfo.transaction request(@RequestBody PgClientDto.PgPaymentRequest request);

	PaymentInfo.order findOrder(final String orderId);

	PaymentInfo.transaction findTransaction(@RequestBody PgClientDto.PgPaymentTransaction request);
}
