package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.loopers.domain.common.*;
import com.loopers.domain.coupon.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loopers.application.order.OrderCriteria;
import com.loopers.application.order.OrderFacade;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandName;
import com.loopers.domain.point.Point;
import com.loopers.domain.product.LikeCount;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductName;
import com.loopers.domain.product.ProductRepository;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.coupon.CouponJpaRepository;
import com.loopers.infrastructure.order.OrderItemJpaRepository;
import com.loopers.infrastructure.order.OrderJpaRepository;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@DisplayName("주문 동시성 통합 테스트")
@ActiveProfiles("test")
@SpringBootTest
public class OrderConcurrencyTest {

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderJpaRepository orderJpaRepository;

	@Autowired
	private OrderItemJpaRepository orderItemJpaRepository;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private BrandJpaRepository brandRepository;

	@Autowired
	private PointJpaRepository pointJpaRepository;

	@Autowired
	private CouponJpaRepository couponJpaRepository;

	@Autowired
	private ProductRepository productRepository;

	private Product product;
	private Point point;

	@BeforeEach
	void setUp() {
		productJpaRepository.deleteAll();
		brandRepository.deleteAll();
		couponJpaRepository.deleteAll();
		pointJpaRepository.deleteAll();
		orderJpaRepository.deleteAll();

		Brand brand = new Brand(new BrandName("나이키"));
		Brand brand1 = brandRepository.save(brand);

		product = new Product(new BrandId(brand1.getId()), new ProductName("티셔츠"),
			new Price(100L), new LikeCount(10L), new Quantity(10L));

		point = new Point(new UserId(1L), 10000L);

		pointJpaRepository.save(point);
		productJpaRepository.save(product);
	}

	@Test
	@DisplayName("동일한 상품에 대해 여러 주문이 동시에 요청되어도, 재고가 정상적으로 차감되어야 한다")
	void createLike_concurrency_with_lock() throws InterruptedException {
		// given
		int numberOfThreads = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		AtomicInteger successCount = new AtomicInteger(0);
		AtomicInteger failCount = new AtomicInteger(0);
		Long userId = 1L;
		Long productId = 1L;

		// when
		for (int i = 0; i < numberOfThreads; i++) {
			executorService.submit(() -> {
				try {
					OrderCriteria.CreateOrder createOrder = new OrderCriteria
						.CreateOrder(userId, List.of(new OrderCriteria.CreateOrder.OrderItem(1L, 1L)), null);

					orderFacade.createOrder(createOrder);
					successCount.incrementAndGet();
				} catch (Exception e) {
					failCount.incrementAndGet();
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();

		// then
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

		assertThat(product.getQuantity().quantity()).isZero();
		assertThat(successCount.get()).isEqualTo(numberOfThreads);
		assertThat(failCount.get()).isEqualTo(0);
	}

    @Test
    @DisplayName("서로 다른 주문을 동시에 수행해도, 포인트가 정상적으로 차감되어야 한다.")
    void createOrder_concurrency_with_points() throws InterruptedException {
        // given
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        Long userId = 1L;
        Long productId = 1L;

        long initialPoints = point.getBalance();
        long pointsToUsePerOrder = 100L;

        // when
        for (int i = 0; i < numberOfThreads; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    OrderCriteria.CreateOrder createOrder = new OrderCriteria
                            .CreateOrder(userId, List.of(new OrderCriteria.CreateOrder.OrderItem(productId, 1L)), null);

                    orderFacade.createOrder(createOrder);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        Point resultPoint = pointJpaRepository.findByUserId(1L).orElseThrow();

        assertThat(resultPoint.getBalance()).isEqualTo(initialPoints - (successCount.get() * pointsToUsePerOrder));
        assertThat(successCount.get()).isEqualTo(numberOfThreads);
        assertThat(failCount.get()).isZero();
    }

}
