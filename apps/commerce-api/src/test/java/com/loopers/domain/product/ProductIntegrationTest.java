package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import com.github.benmanes.caffeine.cache.Cache;
import com.loopers.domain.common.BrandId;
import com.loopers.domain.common.Price;
import com.loopers.domain.common.Quantity;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import com.loopers.utils.RedisCleanUp;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("상품 통합 테스트")
class ProductIntegrationTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private Cache<String, Object> productL1Cache;

	@Autowired
	private RedisTemplate<String, Object> productL2Cache;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@Autowired
	private RedisCleanUp redisCleanUp;

	@MockitoSpyBean
	private ProductRepository productRepository;

	private Product testProduct;
	private ProductCommand.GetProductList command;
	private String listCacheKey;
	private String detailCacheKey;

	@BeforeEach
	void setup() {
		cleanupCaches();

		List<Product> testProducts = createTestProducts();
		testProducts = productJpaRepository.saveAll(testProducts);

		setupTestData(testProducts);

		// Spy 메서드 호출 카운트 초기화
		clearInvocations(productRepository);
	}

	private void cleanupCaches() {
		databaseCleanUp.truncateAllTables();
		productL1Cache.invalidateAll();
		redisCleanUp.truncateAll();
	}

	private List<Product> createTestProducts() {
		return List.of(
			createProduct("브랜드1 상품A", 10000L, 5L, 100L, 1L),
			createProduct("브랜드1 상품B", 15000L, 3L, 80L, 1L),
			createProduct("브랜드1 상품C", 20000L, 7L, 60L, 1L),
			createProduct("브랜드2 상품A", 25000L, 10L, 40L, 2L),
			createProduct("브랜드2 상품B", 30000L, 8L, 30L, 2L),
			createProduct("브랜드3 상품A", 12000L, 4L, 90L, 3L)
		);
	}

	private Product createProduct(String name, Long price, Long likeCount, Long quantity, Long brandId) {
		return Product.builder()
			.name(new ProductName(name))
			.price(new Price(price))
			.likeCount(new LikeCount(likeCount))
			.quantity(new Quantity(quantity))
			.brandId(new BrandId(brandId))
			.build();
	}

	private void setupTestData(List<Product> testProducts) {
		testProduct = testProducts.getFirst();

		Pageable pageable = PageRequest.of(0, 10);
		command = new ProductCommand.GetProductList(1L, pageable);
		listCacheKey = "productList:1:0:10";
		detailCacheKey = "productDetail:" + testProduct.getId();
	}

	@DisplayName("GET 요청")
	@Nested
	class Get {
		@DisplayName("상품 목록 조회 시,")
		@Nested
		class getProductList {
			@Test
			@DisplayName("L1, L2 캐시가 모두 동작하는지 확인")
			void shouldCacheProductListInBothL1AndL2() {
				// when - 첫 번째 조회 (DB 조회 후 L1, L2 캐시 저장)
				Page<ProductInfo> firstResult = productService.getProductList(command);

				// then - 브랜드1 상품 3개 조회 확인 (DB 조회)
				assertThat(firstResult.getContent())
					.hasSize(3)
					.first()
					.extracting(ProductInfo::productName)
					.extracting(ProductName::name)
					.isEqualTo("브랜드1 상품A");

				// then - L1, L2 캐시 모두 저장 확인
				assertThat(productL1Cache.getIfPresent(listCacheKey)).isNotNull();
				assertThat(productL2Cache.hasKey(listCacheKey)).isTrue();

				// then - DB 호출 1회 확인
				verify(productRepository, times(1)).getProductList(eq(1L), any(Pageable.class));

				// when - 두 번째 조회 (L1 캐시에서 조회)
				Page<ProductInfo> secondResult = productService.getProductList(command);

				// then - 동일한 결과 반환 확인 (캐시된 데이터)
				assertThat(secondResult.getContent()).hasSize(3);
				assertThat(secondResult.getContent().getFirst().productId())
					.isEqualTo(firstResult.getContent().getFirst().productId());

				// then - DB 호출이 추가로 발생하지 않았는지 확인 (여전히 1회)
				verify(productRepository, times(1)).getProductList(eq(1L), any(Pageable.class));
			}

			@Test
			@DisplayName("1-3페이지만 캐싱되고 4페이지 이상은 캐시되지 않는지 확인")
			void shouldOnlyCacheFirstThreePages() {
				// when - 1페이지 조회 (캐시됨)
				Pageable page1 = PageRequest.of(0, 10);
				ProductCommand.GetProductList command1 = new ProductCommand.GetProductList(1L, page1);
				productService.getProductList(command1);

				// when - 4페이지 조회 (캐시 안됨)
				Pageable page4 = PageRequest.of(3, 10);
				ProductCommand.GetProductList command4 = new ProductCommand.GetProductList(1L, page4);
				productService.getProductList(command4);

				// then - 1페이지는 캐시됨
				String page1CacheKey = "productList:1:0:10";
				Object page1Cache = productL1Cache.getIfPresent(page1CacheKey);
				assertThat(page1Cache).isNotNull();

				// then - 4페이지는 캐시 안됨
				String page4CacheKey = "productList:1:3:10";
				Object page4Cache = productL1Cache.getIfPresent(page4CacheKey);
				assertThat(page4Cache).isNull();
			}

			@Test
			@DisplayName("L1 캐시 무효화 후 L2 캐시에서 조회하는지 확인")
			void shouldFetchFromL2CacheAfterL1CacheInvalidation() {
				// given - 첫 번째 조회로 L1, L2 캐시 생성
				Page<ProductInfo> firstResult = productService.getProductList(command);
				assertThat(firstResult.getContent()).hasSize(3);

				// L1, L2 캐시 모두 저장되었는지 확인
				assertThat(productL1Cache.getIfPresent(listCacheKey)).isNotNull();
				// L2 캐시에 CacheablePage로 저장되어 있는지 확인
				assertThat(productL2Cache.hasKey(listCacheKey)).isTrue();

				// then - 첫 번째 조회에서 DB 호출 1회 확인
				verify(productRepository, times(1)).getProductList(eq(1L), any(Pageable.class));

				// when - L1 캐시만 무효화 (L2는 유지)
				productL1Cache.invalidate(listCacheKey);
				assertThat(productL1Cache.getIfPresent(listCacheKey)).isNull();
				// L2 캐시에 CacheablePage로 저장되어 있는지 확인
				assertThat(productL2Cache.hasKey(listCacheKey)).isTrue();

				// when - 다시 조회 (L2 캐시에서 조회 후 L1에 저장)
				Page<ProductInfo> secondResult = productService.getProductList(command);

				// then - L2 캐시에서 조회되어 같은 결과 반환
				assertThat(secondResult.getContent()).hasSize(3);
				assertThat(secondResult.getContent().getFirst().productId())
					.isEqualTo(firstResult.getContent().getFirst().productId());

				// L1 캐시에 다시 저장되었는지 확인
				assertThat(productL1Cache.getIfPresent(listCacheKey)).isNotNull();

				// then - DB 호출이 추가로 발생하지 않았는지 확인 (여전히 1회)
				verify(productRepository, times(1)).getProductList(eq(1L), any(Pageable.class));
			}

			@Test
			@DisplayName("L1, L2 캐시 모두 무효화 후 DB에서 조회하는지 확인")
			void shouldFetchFromDBAfterBothCachesInvalidation() {
				// given - 첫 번째 조회로 캐시 생성
				Page<ProductInfo> firstResult = productService.getProductList(command);
				assertThat(firstResult.getContent()).hasSize(3);

				// then - 첫 번째 조회에서 DB 호출 1회 확인
				verify(productRepository, times(1)).getProductList(eq(1L), any(Pageable.class));

				// when - L1, L2 캐시 모두 무효화
				productL1Cache.invalidate(listCacheKey);
				productL2Cache.delete(listCacheKey);
				assertThat(productL1Cache.getIfPresent(listCacheKey)).isNull();
				assertThat(productL2Cache.hasKey(listCacheKey)).isFalse();

				// when - 다시 조회 (DB에서 조회)
				Page<ProductInfo> secondResult = productService.getProductList(command);

				// then - DB에서 다시 조회되어 같은 결과 반환
				assertThat(secondResult.getContent()).hasSize(3);
				assertThat(secondResult.getContent().getFirst().productId())
					.isEqualTo(firstResult.getContent().getFirst().productId());

				// then - DB 호출이 한 번 더 발생해서 총 2회인지 확인
				verify(productRepository, times(2)).getProductList(eq(1L), any(Pageable.class));

				// 캐시에 다시 저장되었는지 확인
				assertThat(productL1Cache.getIfPresent(listCacheKey)).isNotNull();
				// L2 캐시에 CacheablePage로 저장되어 있는지 확인
				assertThat(productL2Cache.hasKey(listCacheKey)).isTrue();
			}

			@Test
			@DisplayName("다른 브랜드 상품들은 별도로 캐싱된다")
			void shouldCacheSeparatelyByBrand() {
				// when - 브랜드 1 조회
				Pageable pageable = PageRequest.of(0, 10);
				ProductCommand.GetProductList brand1Command = new ProductCommand.GetProductList(1L, pageable);
				Page<ProductInfo> brand1Result = productService.getProductList(brand1Command);

				// when - 브랜드 2 조회
				ProductCommand.GetProductList brand2Command = new ProductCommand.GetProductList(2L, pageable);
				Page<ProductInfo> brand2Result = productService.getProductList(brand2Command);

				// then - 각각 다른 결과 반환
				assertThat(brand1Result.getContent()).hasSize(3);
				assertThat(brand1Result.getContent().getFirst().productName().name()).isEqualTo("브랜드1 상품A");

				assertThat(brand2Result.getContent()).hasSize(2);
				assertThat(brand2Result.getContent().getFirst().productName().name()).isEqualTo("브랜드2 상품A");

				// then - 각각 다른 캐시 키로 L1, L2에 저장됨
				String brand1CacheKey = "productList:1:0:10";
				String brand2CacheKey = "productList:2:0:10";

				// L1 캐시 확인
				assertThat(productL1Cache.getIfPresent(brand1CacheKey)).isNotNull();
				assertThat(productL1Cache.getIfPresent(brand2CacheKey)).isNotNull();

				// L2 캐시 확인
				assertThat(productL2Cache.hasKey(brand1CacheKey)).isTrue();
				assertThat(productL2Cache.hasKey(brand2CacheKey)).isTrue();
			}

			@Test
			@DisplayName("캐시 키가 올바르게 생성되는지 확인")
			void shouldGenerateCorrectCacheKey() {
				// when - 특정 조건으로 조회
				Pageable customPageable = PageRequest.of(1, 5);
				ProductCommand.GetProductList customCommand = new ProductCommand.GetProductList(1L, customPageable);
				productService.getProductList(customCommand);

				// then - 예상 캐시 키로 L1, L2에 저장되었는지 확인
				String expectedCacheKey = "productList:1:1:5";
				Object l1CachedValue = productL1Cache.getIfPresent(expectedCacheKey);
				assertThat(l1CachedValue).isNotNull();
				assertThat(productL2Cache.hasKey(expectedCacheKey)).isTrue();
			}

			@Test
			@DisplayName("브랜드 ID가 null인 경우에도 정상 동작")
			void shouldWorkWithNullBrandId() {
				// when - brandId null로 조회
				Pageable pageable = PageRequest.of(0, 10);
				ProductCommand.GetProductList nullBrandCommand = new ProductCommand.GetProductList(null, pageable);
				Page<ProductInfo> result = productService.getProductList(nullBrandCommand);

				// then - 모든 브랜드 상품 조회
				assertThat(result.getContent()).isNotEmpty();

				// 캐시 키에 null 브랜드가 반영되어 L1, L2에 저장되는지 확인
				String expectedCacheKey = "productList:null:0:10";
				Object l1CachedValue = productL1Cache.getIfPresent(expectedCacheKey);
				assertThat(l1CachedValue).isNotNull();
				assertThat(productL2Cache.hasKey(expectedCacheKey)).isTrue();
			}
		}

		@DisplayName("상품 상세 조회 시,")
		@Nested
		class getProductDetail {
			@Test
			@DisplayName("상품 상세 조회 시 L1, L2 캐시가 모두 동작하는지 확인")
			void shouldCacheProductDetailInBothL1AndL2() {
				// when - 첫 번째 조회 (DB 조회 후 L1, L2 캐시 저장)
				ProductInfo firstResult = productService.getProductDetail(testProduct.getId());

				// then - 결과 검증
				assertThat(firstResult.productName().name()).isEqualTo("브랜드1 상품A");
				assertThat(firstResult.price().price()).isEqualTo(10000L);

				// L1 캐시에 저장되었는지 확인
				Object l1CachedValue = productL1Cache.getIfPresent(detailCacheKey);
				assertThat(l1CachedValue).isNotNull();

				// L2 캐시에도 저장되었는지 확인 (상품 상세는 ProductInfo 객체)
				Boolean l2DetailCacheExists = productL2Cache.hasKey(detailCacheKey);
				assertThat(l2DetailCacheExists).isTrue();

				// when - 두 번째 조회 (L1 캐시에서 조회)
				ProductInfo secondResult = productService.getProductDetail(testProduct.getId());

				// then - 캐시된 결과 반환 확인
				assertThat(secondResult.productId()).isEqualTo(firstResult.productId());
				assertThat(secondResult.productName().name()).isEqualTo(firstResult.productName().name());
			}

			@Test
			@DisplayName("상품 상세 L1 캐시 무효화 후 L2 캐시에서 조회하는지 확인")
			void shouldFetchProductDetailFromL2CacheAfterL1CacheInvalidation() {
				// given - 첫 번째 조회로 L1, L2 캐시 생성
				ProductInfo firstResult = productService.getProductDetail(testProduct.getId());

				// when - L1 캐시만 무효화
				productL1Cache.invalidate(detailCacheKey);
				assertThat(productL1Cache.getIfPresent(detailCacheKey)).isNull();
				assertThat(productL2Cache.hasKey(detailCacheKey)).isTrue();

				// when - 다시 조회 (L2 캐시에서 조회)
				ProductInfo secondResult = productService.getProductDetail(testProduct.getId());

				// then - L2 캐시에서 조회되어 같은 결과 반환
				assertThat(secondResult.productId()).isEqualTo(firstResult.productId());
				assertThat(secondResult.productName().name()).isEqualTo(firstResult.productName().name());

				// L1 캐시에 다시 저장되었는지 확인
				assertThat(productL1Cache.getIfPresent(detailCacheKey)).isNotNull();
			}

			@Test
			@DisplayName("존재하지 않는 상품 조회 시 예외 발생")
			void shouldThrowExceptionWhenProductNotFound() {
				// given
				Long nonExistentProductId = 999999L;

				// when and then
				assertThatThrownBy(() -> productService.getProductDetail(nonExistentProductId))
					.hasMessageContaining("상품을 찾을 수 없습니다");
			}
		}
	}

}
