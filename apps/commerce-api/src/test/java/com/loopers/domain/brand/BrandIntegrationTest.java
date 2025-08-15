package com.loopers.domain.brand;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import com.github.benmanes.caffeine.cache.Cache;
import com.loopers.domain.common.BrandId;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import com.loopers.utils.RedisCleanUp;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("브랜드 통합 테스트")
public class BrandIntegrationTest {

	@Autowired
	private BrandService brandService;

	@Autowired
	private BrandJpaRepository brandJpaRepository;

	@Autowired
	private Cache<String, Object> brandL1Cache;

	@Autowired
	private RedisTemplate<String, Object> brandL2Cache;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@Autowired
	private RedisCleanUp redisCleanUp;

	@MockitoSpyBean
	private BrandRepository brandRepository;

	private Brand testBrand;
	private String detailCacheKey;

	@BeforeEach
	void setup() {
		cleanupCaches();

		List<Brand> testBrands = createTestBrands();
		testBrands = brandJpaRepository.saveAll(testBrands);

		setupTestData(testBrands);

		// Spy 메서드 호출 카운트 초기화
		clearInvocations(brandRepository);
	}

	private void cleanupCaches() {
		databaseCleanUp.truncateAllTables();
		brandL1Cache.invalidateAll();
		redisCleanUp.truncateAll();
	}

	private List<Brand> createTestBrands() {
		return List.of(
			createBrand("나이키"),
			createBrand("아디다스"),
			createBrand("뉴발란스")
		);
	}

	private Brand createBrand(String name) {
		return Brand.builder()
			.brandName(new BrandName(name))
			.build();
	}

	private void setupTestData(List<Brand> testBrands) {
		testBrand = testBrands.getFirst();
		detailCacheKey = "brand:" + testBrand.getId();
	}

	@DisplayName("GET 요청")
	@Nested
	class Get {

		@DisplayName("브랜드 상세 조회 시,")
		@Nested
		class getBrandById {

			@Test
			@DisplayName("L1, L2 캐시가 모두 동작하는지 확인")
			void shouldCacheBrandDetailInBothL1AndL2() {
				// when - 첫 번째 조회 (DB 조회 후 L1, L2 캐시 저장)
				BrandInfo firstResult = brandService.getBrandById(new BrandId(testBrand.getId()));

				// then - 결과 검증
				assertThat(firstResult.brandName().brandName()).isEqualTo("나이키");

				// then - L1, L2 캐시 모두 저장 확인
				assertThat(brandL1Cache.getIfPresent(detailCacheKey)).isNotNull();
				assertThat(brandL2Cache.hasKey(detailCacheKey)).isTrue();

				// then - DB 호출 1회 확인
				verify(brandRepository, times(1)).findById(new BrandId(testBrand.getId()));

				// when - 두 번째 조회 (L1 캐시에서 조회)
				BrandInfo secondResult = brandService.getBrandById(new BrandId(testBrand.getId()));

				// then - 캐시된 결과 반환 확인
				assertThat(secondResult.brandId()).isEqualTo(firstResult.brandId());

				// then - DB 호출이 추가로 발생하지 않았는지 확인 (여전히 1회)
				verify(brandRepository, times(1)).findById(new BrandId(testBrand.getId()));
			}

			@Test
			@DisplayName("L1 캐시 무효화 후 L2 캐시에서 조회하는지 확인")
			void shouldFetchDetailFromL2CacheAfterL1CacheInvalidation() {
				// given - 첫 번째 조회로 L1, L2 캐시 생성
				brandService.getBrandById(new BrandId(testBrand.getId()));
				verify(brandRepository, times(1)).findById(new BrandId(testBrand.getId()));
				assertThat(brandL1Cache.getIfPresent(detailCacheKey)).isNotNull();
				assertThat(brandL2Cache.hasKey(detailCacheKey)).isTrue();

				// when - L1 캐시만 무효화
				brandL1Cache.invalidate(detailCacheKey);
				assertThat(brandL1Cache.getIfPresent(detailCacheKey)).isNull();

				// when - 다시 조회 (L2 캐시에서 조회 후 L1에 저장)
				brandService.getBrandById(new BrandId(testBrand.getId()));

				// then - DB 호출이 추가로 발생하지 않았는지 확인 (여전히 1회)
				verify(brandRepository, times(1)).findById(new BrandId(testBrand.getId()));
				assertThat(brandL1Cache.getIfPresent(detailCacheKey)).isNotNull();
			}

			@Test
			@DisplayName("L1, L2 캐시 모두 무효화 후 DB에서 조회하는지 확인")
			void shouldFetchDetailFromDBAfterBothCachesInvalidation() {
				// given - 첫 번째 조회로 캐시 생성
				brandService.getBrandById(new BrandId(testBrand.getId()));
				verify(brandRepository, times(1)).findById(new BrandId(testBrand.getId()));

				// when - L1, L2 캐시 모두 무효화
				brandL1Cache.invalidate(detailCacheKey);
				brandL2Cache.delete(detailCacheKey);
				assertThat(brandL1Cache.getIfPresent(detailCacheKey)).isNull();
				assertThat(brandL2Cache.hasKey(detailCacheKey)).isFalse();

				// when - 다시 조회 (DB에서 조회)
				brandService.getBrandById(new BrandId(testBrand.getId()));

				// then - DB 호출이 한 번 더 발생해서 총 2회인지 확인
				verify(brandRepository, times(2)).findById(new BrandId(testBrand.getId()));

				// then - 캐시에 다시 저장되었는지 확인
				assertThat(brandL1Cache.getIfPresent(detailCacheKey)).isNotNull();
				assertThat(brandL2Cache.hasKey(detailCacheKey)).isTrue();
			}

			@Test
			@DisplayName("존재하지 않는 브랜드 조회 시 예외 발생")
			void shouldThrowExceptionWhenBrandNotFound() {
				// given
				Long nonExistentBrandId = 999999L;

				// when and then
				assertThatThrownBy(() -> brandService.getBrandById(new BrandId(nonExistentBrandId)))
					.isInstanceOf(RuntimeException.class) // 실제 사용하는 예외 클래스로 변경 가능
					.hasMessageContaining("브랜드를 찾을 수 없습니다");
			}
		}
	}
}
