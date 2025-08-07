package com.loopers.domain.order;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.common.Price;
import com.loopers.domain.common.ProductId;
import com.loopers.domain.common.Quantity;
import com.loopers.domain.common.UserId;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;
	private final PointRepository pointRepository;
	private final CouponRepository couponRepository;

	@Transactional
	public OrderInfo createOrder(final OrderCommand.CreateOrder command) {

		// 요청된 상품 정보로 OrderItem 리스트 생성
		List<OrderItem> orderItems = command.items().stream().map(request -> {
			Product product = productRepository.findById(request.productId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

			// 재고 차감
			Long quantity = request.quantity();
			product.decreaseStock(quantity);

			return OrderItem.builder()
				.productId(new ProductId(product.getId()))
				.quantity(new Quantity(product.getQuantity().quantity()))
				.price(new Price(product.getPrice().price()))
				.build();
		}).toList();

		// 사용자 포인트 확인
		Point point = pointRepository.findByUsersId(command.userId())
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, " 해당 [id = " + command.userId() + "]의 포인트가 존재하지 않습니다."));

		// 쿠폰 확인
		Coupon coupon = couponRepository.findById(command.userId())
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, " 해당 [id = " + command.userId() + "]의 쿠폰이 존재하지 않습니다."));

		// 쿠폰 사용
		coupon.use();

		// 포인트 차감
		Long balance = point.getBalance();
		point.use(balance);

		// 주문 총 가격 계산
		Long totalPrice = orderItems.stream()
			.mapToLong(item -> item.getPrice().price() * item.getQuantity().quantity())
			.sum();

		// 주문 저장
		Order newOrder = Order.builder()
			.userId(new UserId(command.userId()))
			.totalOrderPrice(new TotalOrderPrice(totalPrice))
			.status(OrderStatus.PENDING)
			.build();

		Order order = orderRepository.save(newOrder);
		List<OrderItem> orderItemList = orderItemRepository.saveAll(orderItems);

		return OrderInfo.from(order, orderItemList);
	}

	@Transactional(readOnly = true)
	public List<OrderInfo> getOrders(final OrderCommand.GetOrders command) {
		List<Order> orderList = orderRepository.findAllOrdersByUserId(command.userId());

		if (orderList.isEmpty()) {
			return Collections.emptyList();
		}

		List<Long> orderIds = orderList.stream()
			.map(Order::getId)
			.collect(Collectors.toList());

		List<OrderItem> allOrderItems = orderItemRepository.findAllByOrderIdIn(orderIds);

		Map<Long, List<OrderItem>> orderItemMap = allOrderItems.stream()
			.collect(Collectors.groupingBy(orderItem -> orderItem.getOrderId().orderId()));

		return orderList.stream()
			.map(order -> {
				List<OrderItem> orderItems = orderItemMap.getOrDefault(order.getId(), Collections.emptyList());
				return OrderInfo.from(order, orderItems);
			})
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public OrderInfo getOrder(final OrderCommand.GetOrder command) {
		Order order = orderRepository.findByIdAndUserId(command.orderId(), command.userId())
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "주문을 찾을 수 없습니다."));

		List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(command.orderId());

		return OrderInfo.from(order, orderItems);
	}
}
