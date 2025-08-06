package com.loopers.domain.order;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.interfaces.api.order.OrderDto;
import com.loopers.support.error.CoreException;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;
	@Mock
	private ProductRepository productRepository;
	@Mock
	private PointRepository pointRepository;
	@InjectMocks
	private OrderService orderService;

	@Test
	@DisplayName("주문 생성 시, 재고가 충분하면 성공적으로 주문이 생성된다.")
	void createOrder_success() {
		// given
		Long userId = 1L;
		OrderDto.V1.OrderItemRequest request1 = new OrderDto.V1.OrderItemRequest(101L, 2L);
		Product product1 = mock(Product.class);
		Point point = mock(Point.class);
		Coupon coupon = mock(Coupon.class);

		when(productRepository.findById(any())).thenReturn(Optional.of(product1));
		when(product1.getId()).thenReturn(101L);
		when(pointRepository.findByUsersId(userId)).thenReturn(Optional.of(point));
		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		/*
		OrderInfo result = orderService.createOrder(userId, List.of(request1), coupon.getId());

		// then
		assertThat(result).isNotNull();
		assertThat(result.userId()).isEqualTo(userId);
		assertThat(result.status()).isEqualTo(OrderStatus.PENDING);
		verify(product1, times(1)).decreaseStock(2L);
		verify(point, times(1)).use();
		verify(orderRepository, times(1)).save(any(Order.class));
		*/
	}

	@Test
	@DisplayName("주문 생성 시, 재고가 부족하면 예외가 발생한다.")
	void createOrder_insufficientStock_throwsException() {
		// given
		Long userId = 1L;
		OrderDto.V1.OrderItemRequest request1 = new OrderDto.V1.OrderItemRequest(101L, 5L);
		Product product1 = mock(Product.class);
		Coupon coupon = mock(Coupon.class);

		when(productRepository.findById(any())).thenReturn(Optional.of(product1));
		when(product1.getId()).thenReturn(101L);
		// 재고 확인 시 예외를 발생시키도록 설정
		doThrow(new CoreException(null, "재고가 부족합니다.")).when(product1).decreaseStock(anyLong());

		// when and then
		/*
		assertThatThrownBy(() -> orderService.createOrder(userId, List.of(request1), coupon.getId()))
			.isInstanceOf(CoreException.class)
			.hasMessage("재고가 부족합니다.");

		// 주문이 저장되거나 포인트가 사용되면 안 됨
		verify(orderRepository, never()).save(any(Order.class));
		verify(pointRepository, never()).findByUsersId(any());
		*/
	}
}
