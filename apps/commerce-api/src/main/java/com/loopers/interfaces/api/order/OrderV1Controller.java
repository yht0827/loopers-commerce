package com.loopers.interfaces.api.order;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.order.OrderCriteria;
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
	public ApiResponse<OrderDto.V1.OrderResponse> createOrder(@RequestHeader final Long userId,
		@RequestBody final OrderDto.V1.OrderRequest orderRequest) {
		OrderCriteria.CreateOrder criteria = orderRequest.toCriteria(userId);
		OrderResult orderResult = orderFacade.createOrder(criteria);
		OrderDto.V1.OrderResponse orderResponse = OrderDto.V1.OrderResponse.from(orderResult);

		return ApiResponse.success(orderResponse);
	}

	@GetMapping
	public ApiResponse<List<OrderDto.V1.OrderResponse>> getOrders(@RequestHeader final Long userId) {
		OrderCriteria.GetOrders criteria = OrderDto.V1.getOrdersRequest.toCriteria(userId);
		List<OrderResult> orderResults = orderFacade.getOrders(criteria);
		List<OrderDto.V1.OrderResponse> responses = orderResults.stream()
			.map(OrderDto.V1.OrderResponse::from)
			.toList();

		return ApiResponse.success(responses);
	}

	@GetMapping("/{orderId}")
	public ApiResponse<OrderDto.V1.OrderResponse> getOrder(@RequestHeader final Long userId, @PathVariable Long orderId) {
		OrderCriteria.GetOrder criteria = OrderDto.V1.getOrderRequest.toCriteria(userId, orderId);
		OrderResult orderDetails = orderFacade.getOrder(criteria);
		OrderDto.V1.OrderResponse orderResponse = OrderDto.V1.OrderResponse.from(orderDetails);
		return ApiResponse.success(orderResponse);
	}

}
