package com.loopers.infrastructure.product;

import static com.loopers.domain.product.QProduct.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.loopers.domain.common.BrandId;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
	private final ProductJpaRepository productJpaRepository;
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Product> findById(final Long id) {
		return productJpaRepository.findById(id);
	}

	@Override
	public Optional<Product> findByIdWithPessimisticLock(Long id) {
		return productJpaRepository.findByIdWithPessimisticLock(id);
	}

	@Override
	public Optional<Product> findByIdWithOptimisticLock(Long id) {
		return productJpaRepository.findByIdWithOptimisticLock(id);
	}

	@Override
	public Page<Product> getProductList(final Long brandId, final Pageable pageable) {
		return getProductListWithCoveringIndex(brandId, pageable);
	}

	// 커버링 인덱스를 사용한 최적화된 상품 목록 조회
	private Page<Product> getProductListWithCoveringIndex(final Long brandId, final Pageable pageable) {
		// 커버링 인덱스로 필요한 컬럼만 조회 (ID + 정렬 필드)
		JPAQuery<Long> idQuery = jpaQueryFactory
			.select(product.id)
			.from(product);

		JPAQuery<Long> countQuery = jpaQueryFactory.select(product.count()).from(product);

		if (brandId != null) {
			BrandId brand = new BrandId(brandId);
			idQuery.where(product.brandId.eq(brand));
			countQuery.where(product.brandId.eq(brand));
		}

		applyOrderByForIdQuery(idQuery, pageable);

		List<Long> productIds = idQuery
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// ID 기반으로 전체 데이터 조회 (순서 보장)
		if (productIds.isEmpty()) {
			return PageableExecutionUtils.getPage(List.of(), pageable, countQuery::fetchOne);
		}

		// ID 리스트 순서대로 Map 생성하여 정렬 보장
		List<Product> allProducts = jpaQueryFactory
			.selectFrom(product)
			.where(product.id.in(productIds))
			.fetch();

		// ID 순서에 맞게 정렬
		List<Product> products = productIds.stream()
			.map(id -> allProducts.stream()
				.filter(p -> p.getId().equals(id))
				.findFirst()
				.orElse(null))
			.filter(java.util.Objects::nonNull)
			.toList();

		return PageableExecutionUtils.getPage(products, pageable, countQuery::fetchOne);
	}

	private void applyOrderByForIdQuery(JPAQuery<Long> query, Pageable pageable) {
		pageable.getSort().stream()
			.map(this::createOrderSpecifierForIdQuery)
			.forEach(query::orderBy);
	}

	private OrderSpecifier<?> createOrderSpecifierForIdQuery(org.springframework.data.domain.Sort.Order order) {
		Order direction = order.isAscending() ? Order.ASC : Order.DESC;

		return switch (order.getProperty()) {
			case "price" -> new OrderSpecifier<>(direction, product.price.price);
			case "likesCount" -> new OrderSpecifier<>(direction, product.likeCount.likeCount);
			default -> new OrderSpecifier<>(direction, product.createdAt);
		};
	}

}
