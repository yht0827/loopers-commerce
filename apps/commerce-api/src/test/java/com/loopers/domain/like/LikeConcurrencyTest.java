package com.loopers.domain.like;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
}
