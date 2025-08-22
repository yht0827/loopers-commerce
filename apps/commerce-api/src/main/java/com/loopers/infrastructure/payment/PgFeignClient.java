package com.loopers.infrastructure.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.loopers.interfaces.api.ApiResponse;

@FeignClient(name = "pgClient", url = "http://localhost:8082/api/v1/payments")
public interface PgFeignClient {

	@PostMapping
	ApiResponse<PgClientDto.PgPaymentTransaction> request(PgClientDto.PgPaymentRequest request);

	@GetMapping
	ApiResponse<PgClientDto.PgPaymentOrderResponse> findOrder(@RequestParam(name = "transactionKey") String transactionKey);

	@GetMapping("/{transactionKey}")
	ApiResponse<PgClientDto.PgPaymentTransaction> findTransaction(@PathVariable(name = "transactionKey") String transactionKey);
}
