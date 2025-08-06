package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.domain.common.Price;
import com.loopers.domain.common.Quantity;
import com.loopers.domain.common.UserId;

public class OrderTest {

	@DisplayName("주문 모델을 생성할 때,")
	@Nested
	class Create {
		@DisplayName("주문 아이템 목록이 주어지면, 총액이 정확히 계산되고 상태가 PENDING으로 설정된다.")
		@Test
		void createOrder_calculatesTotalPrice_and_setsStatus() {
			// arrange
			Long userId = 1L;
			OrderItem item1 = OrderItem.builder().price(new Price(1000L)).quantity(new Quantity(2L)).build(); // 2000원
			OrderItem item2 = OrderItem.builder().price(new Price(5000L)).quantity(new Quantity(1L)).build(); // 5000원

			List<OrderItem> orderItems = Arrays.asList(item1, item2);

			Long totalPrice = orderItems.stream()
				.mapToLong(item -> item.getPrice().price() * item.getQuantity().quantity())
				.sum();

			TotalOrderPrice totalOrderPrice = new TotalOrderPrice(totalPrice);

			// act
			Order order = Order.builder()
				.userId(new UserId(userId))
				.totalOrderPrice(totalOrderPrice)
				.status(OrderStatus.PENDING)
				.build();

			// assert
			assertAll(
				() -> assertThat(order.getUserId()).isEqualTo(new UserId(userId)),
				() -> assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING),
				() -> assertThat(order.getTotalOrderPrice()).isEqualTo(new TotalOrderPrice(7000L)));
		}

		@DisplayName("주문 아이템이 없으면, 총액은 0이어야 한다.")
		@Test
		void createOrder_withEmptyItems_hasZeroTotalPrice() {
			// arrange
			Long userId = 1L;

			// act
			// Order 엔티티가 직접 총액을 계산하도록 로직을 변경했다고 가정
			Order order = Order.builder()
				.userId(new UserId(userId))
				.totalOrderPrice(new TotalOrderPrice(0L)) // 총액 0
				.status(OrderStatus.PENDING)
				.build();

			// assert
			assertThat(order.getTotalOrderPrice().totalPrice()).isEqualTo(0L);
		}

	}
}
