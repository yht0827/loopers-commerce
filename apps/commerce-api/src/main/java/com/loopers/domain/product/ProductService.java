package com.loopers.domain.product;

import java.time.Duration;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.loopers.domain.order.OrderItem;
import com.loopers.domain.product.event.ProductOutOfStockEvent;
import com.loopers.support.cache.CacheablePage;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.support.event.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

	private static final Duration L2_PRODUCT_LIST_TTL = Duration.ofMinutes(30);
	private static final Duration L2_PRODUCT_DETAIL_TTL = Duration.ofMinutes(60);
	private static final String CACHE_KEY_PREFIX_PRODUCT_LIST = "productList";
	private static final String CACHE_KEY_PREFIX_PRODUCT_DETAIL = "productDetail";

	private final ProductRepository productRepository;
	private final Cache<String, Object> productL1Cache;
	private final RedisTemplate<String, Object> productL2Cache;
	private final EventPublisher eventPublisher;

	@SuppressWarnings("unchecked")
	public Page<ProductInfo> getProductList(final ProductCommand.GetProductList command) {
		int pageNumber = command.pageable().getPageNumber();

		// 1-3 페이지만 캐싱 처리
		if (pageNumber >= 0 && pageNumber < 3) {
			String cacheKey = CACHE_KEY_PREFIX_PRODUCT_LIST + ":" +
				command.brandId() + ":" +
				command.pageable().getPageNumber() + ":" +
				command.pageable().getPageSize();

			// L1 캐시 확인
			Page<ProductInfo> l1Result = (Page<ProductInfo>)productL1Cache.getIfPresent(cacheKey);
			if (l1Result != null) {
				return l1Result;
			}

			// L2 캐시 확인
			CacheablePage<ProductInfo> l2CacheResult = (CacheablePage<ProductInfo>)productL2Cache.opsForValue().get(cacheKey);
			if (l2CacheResult != null) {
				Page<ProductInfo> l2Result = l2CacheResult.toPage(command.pageable());
				productL1Cache.put(cacheKey, l2Result);
				return l2Result;
			}

			// 캐시 미스 - DB 조회 및 캐시 저장
			Page<ProductInfo> dbResult = productRepository.getProductList(command.brandId(), command.pageable());

			// 캐시 저장
			CacheablePage<ProductInfo> cacheableResult = CacheablePage.from(dbResult);
			productL2Cache.opsForValue().set(cacheKey, cacheableResult, L2_PRODUCT_LIST_TTL);
			productL1Cache.put(cacheKey, dbResult);

			return dbResult;
		}

		// 4페이지 이상은 캐시 없이 DB 직접 조회
		return productRepository.getProductList(command.brandId(), command.pageable());
	}

	public ProductInfo getProductDetail(final Long productId) {
		String cacheKey = CACHE_KEY_PREFIX_PRODUCT_DETAIL + ":" + productId;

		// L1 캐시 확인 (Cache-Aside)
		ProductInfo l1Result = (ProductInfo)productL1Cache.getIfPresent(cacheKey);
		if (l1Result != null) {
			return l1Result;
		}

		// L2 캐시 확인
		ProductInfo l2Result = (ProductInfo)productL2Cache.opsForValue().get(cacheKey);
		if (l2Result != null) {
			// L1에 다시 저장
			productL1Cache.put(cacheKey, l2Result);
			return l2Result;
		}

		// 캐시 미스 - DB 조회 및 캐시 저장
		log.debug("Cache miss: {}", cacheKey);
		ProductInfo result = productRepository.findById(productId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

		// 캐시 저장 (Write-Through)
		productL2Cache.opsForValue().set(cacheKey, result, L2_PRODUCT_DETAIL_TTL);
		productL1Cache.put(cacheKey, result);

		return result;
	}

	public void deductStock(List<OrderItem> orderItems) {
		for (OrderItem items : orderItems) {
			Product product = productRepository.findByIdWithPessimisticLock(items.getId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

			Long oldQuantity = product.getQuantity().getQuantity();

			product.deduct(items.getQuantity());

			// 품절 상태가 되었을 때만 이벤트 발행
			if (oldQuantity > 0 && product.getQuantity().isOutOfStock()) {
				ProductOutOfStockEvent event = ProductOutOfStockEvent.create(product.getId());
				eventPublisher.publish(event);
				log.info("품절 이벤트 발행: productId={}", product.getId());
			}
		}
	}

	public void evictProductCache(Long productId) {
		String cacheKey = CACHE_KEY_PREFIX_PRODUCT_DETAIL + ":" + productId;

		try {
			productL1Cache.invalidate(cacheKey);
			productL2Cache.delete(cacheKey);
			log.debug("상품 캐시 제거 완료 - productId: {}, key: {}", productId, cacheKey);
		} catch (Exception e) {
			log.warn("상품 캐시 제거 실패 - productId: {}, key: {}", productId, cacheKey, e);
		}
	}

	public void evictProductListCache() {
		try {
			evictL1ProductListCaches();

			String listKeyPattern = CACHE_KEY_PREFIX_PRODUCT_LIST + ":*";
			productL2Cache.delete(listKeyPattern);

			log.debug("상품 리스트 캐시 제거 완료 - pattern: {}", listKeyPattern);
		} catch (Exception e) {
			log.warn("상품 리스트 캐시 제거 실패", e);
		}
	}

	void evictL1ProductListCaches() {
		productL1Cache.asMap().keySet().stream()
			.filter(key -> key.startsWith(CACHE_KEY_PREFIX_PRODUCT_LIST + ":"))
			.forEach(productL1Cache::invalidate);
	}

	public void evictProductRelatedCaches(Long productId) {
		evictProductCache(productId);
		evictProductListCache();
	}
}
