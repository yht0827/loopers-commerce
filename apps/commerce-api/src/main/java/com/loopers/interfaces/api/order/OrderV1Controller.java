package com.loopers.interfaces.api.order;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderResult;
import com.loopers.interfaces.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderV1Controller {

	private final OrderFacade orderFacade;

	@PostMapping
	public ApiResponse<OrderResponse> createOrder(@RequestHeader final Long userId,
		@RequestBody OrderRequest orderRequest) {
		OrderResult orderResult = orderFacade.createOrder(userId, orderRequest.toCommand());
		OrderResponse orderResponse = OrderResponse.from(orderResult);

		return ApiResponse.success(orderResponse);
	}

	@GetMapping
	public ApiResponse<List<OrderResponse>> getOrderList(@RequestHeader final Long userId) {
		List<OrderResult> orderResults = orderFacade.getOrdersById(userId);

		List<OrderResponse> responses = orderResults.stream()
			.map(OrderResponse::from)
			.toList();

		return ApiResponse.success(responses);
	}

	@GetMapping("/{orderId}")
	public ApiResponse<OrderResponse> getOrderDetail(@PathVariable Long orderId, @RequestHeader final Long userId) {
		OrderResult orderDetails = orderFacade.getOrderDetails(userId, orderId);
		OrderResponse orderResponse = OrderResponse.from(orderDetails);
		return ApiResponse.success(orderResponse);
	}

}
