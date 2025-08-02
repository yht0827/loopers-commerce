package com.loopers.domain.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.interfaces.api.order.OrderItemRequest;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final PointRepository pointRepository;

	@Transactional
	public OrderInfo createOrder(Long userId, List<OrderItemRequest> itemRequests) {

		// 1. 요청된 상품 정보로 OrderItem 리스트 생성
		List<OrderItem> orderItems = itemRequests.stream().map(request -> {
			Product product = productRepository.findById(request.productId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

			// 재고 차감
			product.decreaseStock(request.quantity());

			return OrderItem.builder()
				.productId(product.getId())
				.quantity(new Quantity(product.getQuantity().quantity()))
				.price(new Price(product.getPrice().price()))
				.build();
		}).toList();

		// 2. 주문 총 가격 계산
		Long totalPrice = orderItems.stream()
			.mapToLong(item -> item.getPrice().price() * item.getQuantity().quantity())
			.sum();

		// 3. 사용자 포인트 확인
		Point point = pointRepository.findByUsersId(userId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, " 해당 [id = " + userId + "]의 포인트가 존재하지 않습니다."));

		// 4. 포인트 차감
		point.use();

		// 5. 주문 저장
		Order newOrder = Order.builder()
			.userId(userId)
			.totalOrderPrice(new TotalOrderPrice(totalPrice))
			.status(OrderStatus.PENDING)
			.build();

		Order order = orderRepository.save(newOrder);

		return OrderInfo.from(order);
	}

	@Transactional(readOnly = true)
	public List<OrderInfo> getOrdersById(Long userId) {
		List<Order> orderList = orderRepository.findAllOrdersByUserId(userId);
		return orderList.stream().map(OrderInfo::from).toList();
	}

	@Transactional(readOnly = true)
	public OrderInfo getOrderDetails(Long userId, Long orderId) {
		Order order = orderRepository.findByIdAndUserId(orderId, userId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "주문을 찾을 수 없습니다."));

		return OrderInfo.from(order);
	}
}
