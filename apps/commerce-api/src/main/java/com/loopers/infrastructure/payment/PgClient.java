package com.loopers.infrastructure.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.loopers.feign.FeignClientTimeoutConfig;

@FeignClient(name = "pgClient", url = "http://localhost:8082", configuration = FeignClientTimeoutConfig.class)
public interface PgClient {
	@PostMapping("/pay")
	PgClientDto.PgPaymentResponse requestPayment(@RequestBody PgClientDto.PgPaymentRequest request);
}
