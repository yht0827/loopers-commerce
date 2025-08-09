package com.loopers.domain.like;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loopers.application.like.LikeFacade;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandName;
import com.loopers.domain.common.BrandId;
import com.loopers.domain.common.Price;
import com.loopers.domain.common.Quantity;
import com.loopers.domain.product.LikeCount;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductName;
import com.loopers.domain.product.ProductRepository;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@DisplayName("좋아요 동시성 통합 테스트")
@ActiveProfiles("test")
@SpringBootTest
public class LikeConcurrencyTest {

	@Autowired
	private LikeFacade likeFacade;

	@Autowired
	private LikeJpaRepository likeJpaRepository;

	@Autowired
	private BrandJpaRepository brandRepository;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private ProductRepository productRepository;

	private Product product;

	@BeforeEach
	void setUp() {
		likeJpaRepository.deleteAll();
		productJpaRepository.deleteAll();
		brandRepository.deleteAll();

		Brand brand = new Brand(new BrandName("나이키"));
		Brand brand1 = brandRepository.save(brand);

		product = new Product(new BrandId(brand1.getId()), new ProductName("티셔츠"),
			new Price(1000L), new LikeCount(10L), new Quantity(100L));

		productJpaRepository.save(product);
	}

	@Test
	@DisplayName("한 상품에 동시에 10번의 좋아요 요청을 보내도, 좋아요는 1개만 생성되어야 한다")
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
					likeFacade.likeProduct(userId, product.getId());
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

		assertThat(product.getLikeCount().likeCount()).isEqualTo(11L);
		assertThat(successCount.get()).isOne();
		assertThat(failCount.get()).isEqualTo(numberOfThreads - 1);
	}

}
