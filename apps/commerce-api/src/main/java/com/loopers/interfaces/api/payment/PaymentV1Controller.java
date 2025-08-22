package com.loopers.interfaces.api.payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.payment.PaymentCommand;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.application.payment.PaymentResult;
import com.loopers.interfaces.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentV1Controller {

	private final PaymentFacade paymentFacade;

	@PostMapping
	public ApiResponse<PaymentDto.V1.PaymentResponse> createPayment(
		@RequestHeader(value = "X-USER-ID") final String userId,
		@RequestBody final PaymentDto.V1.PaymentRequest paymentRequest) {

		PaymentCommand.CreatePayment command = paymentRequest.toCriteria(userId);
		PaymentResult paymentResult = paymentFacade.createPayment(command);
		PaymentDto.V1.PaymentResponse paymentResponse = PaymentDto.V1.PaymentResponse.from(paymentResult);

		return ApiResponse.success(paymentResponse);
	}

}
