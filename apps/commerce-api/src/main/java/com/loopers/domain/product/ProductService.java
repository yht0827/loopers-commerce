package com.loopers.domain.product;

import java.time.Duration;

import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

	private static final Duration L2_PRODUCT_LIST_TTL = Duration.ofMinutes(30);
	private static final Duration L2_PRODUCT_DETAIL_TTL = Duration.ofMinutes(60);
	private static final String CACHE_KEY_PREFIX_PRODUCT_LIST = "productList:";
	private static final String CACHE_KEY_PREFIX_PRODUCT_DETAIL = "productDetail:";

	private final ProductRepository productRepository;

	private final Cache<String, Object> productL1Cache;
	private final RedisTemplate<String, Object> productL2Cache;

	@SuppressWarnings("unchecked")
	public Page<ProductInfo> getProductList(final ProductCommand.GetProductList command) {
		String cacheKey = CACHE_KEY_PREFIX_PRODUCT_LIST + command.brandId() + ":" +
			command.pageable().getPageNumber() + ":" + command.pageable().getPageSize();

		// L1 캐시 조회
		Page<ProductInfo> l1Result = (Page<ProductInfo>)productL1Cache.getIfPresent(cacheKey);
		if (l1Result != null) {
			return l1Result;
		}

		// L2 캐시 조회
		Page<ProductInfo> l2Result = (Page<ProductInfo>)productL2Cache.opsForValue().get(cacheKey);
		if (l2Result != null) {
			productL1Cache.put(cacheKey, l2Result);
			return l2Result;
		}

		// DB 조회 및 캐시 저장
		log.debug("Cache miss: {}", cacheKey);
		Page<Product> products = productRepository.getProductList(command.brandId(), command.pageable());
		Page<ProductInfo> result = products.map(ProductInfo::from);

		productL2Cache.opsForValue().set(cacheKey, result, L2_PRODUCT_LIST_TTL);
		productL1Cache.put(cacheKey, result);

		return result;
	}

	@SuppressWarnings("unchecked")
	public ProductInfo getProductDetail(final Long productId) {
		String cacheKey = CACHE_KEY_PREFIX_PRODUCT_DETAIL + productId;

		// L1 캐시 조회
		ProductInfo l1Result = (ProductInfo)productL1Cache.getIfPresent(cacheKey);
		if (l1Result != null) {
			return l1Result;
		}

		// L2 캐시 조회
		ProductInfo l2Result = (ProductInfo)productL2Cache.opsForValue().get(cacheKey);
		if (l2Result != null) {
			productL1Cache.put(cacheKey, l2Result);
			return l2Result;
		}

		// DB 조회 및 캐시 저장
		log.debug("Cache miss: {}", cacheKey);
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

		ProductInfo result = ProductInfo.from(product);

		productL2Cache.opsForValue().set(cacheKey, result, L2_PRODUCT_DETAIL_TTL);
		productL1Cache.put(cacheKey, result);

		return result;
	}
}

