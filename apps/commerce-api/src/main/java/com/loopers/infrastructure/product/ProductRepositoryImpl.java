package com.loopers.infrastructure.product;

import static com.loopers.domain.brand.QBrand.*;
import static com.loopers.domain.product.QProduct.*;
import static com.loopers.domain.product.QProductAggregate.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.loopers.domain.common.BrandId;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.QProductInfo;
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
	public Optional<ProductInfo> findById(final Long id) {

		ProductInfo productInfo = jpaQueryFactory.select(
				new QProductInfo(product.id, product.name.name, product.price.price, product.quantity.quantity,
					brand.brandName.brandName, productAggregate.likeCount.likeCount.coalesce(0L)))
			.from(product)
			.leftJoin(brand)
			.on(product.brandId.brandId.eq(brand.id))
			.leftJoin(productAggregate)
			.on(productAggregate.productId.productId.eq(product.id))
			.where(product.id.eq(id))
			.fetchOne();

		return Optional.ofNullable(productInfo);
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
	public Page<ProductInfo> getProductList(final Long brandId, final Pageable pageable) {
		return getProductListWithCoveringIndex(brandId, pageable);
	}

	@Override
	public Product save(final Product product) {
		return productJpaRepository.save(product);
	}

	// 커버링 인덱스를 사용한 최적화된 상품 목록 조회
	private Page<ProductInfo> getProductListWithCoveringIndex(final Long brandId, final Pageable pageable) {
		// 커버링 인덱스로 ID만 조회
		List<Long> productIds = getProductIdsWithCoveringIndex(brandId, pageable);

		// 카운트 쿼리
		JPAQuery<Long> countQuery = createCountQuery(brandId);

		if (productIds.isEmpty()) {
			return PageableExecutionUtils.getPage(List.of(), pageable, countQuery::fetchOne);
		}

		// ProductInfo로 JOIN 조회 (정렬 순서 보장)
		List<ProductInfo> productInfos = getProductInfosByIds(productIds);

		return PageableExecutionUtils.getPage(productInfos, pageable, countQuery::fetchOne);
	}

	private List<Long> getProductIdsWithCoveringIndex(Long brandId, Pageable pageable) {
		JPAQuery<Long> idQuery = jpaQueryFactory.select(product.id).from(product);

		if (brandId != null) {
			BrandId brand = new BrandId(brandId);
			idQuery.where(product.brandId.eq(brand));
		}

		applyOrderByForIdQuery(idQuery, pageable);

		return idQuery.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}

	private JPAQuery<Long> createCountQuery(Long brandId) {
		JPAQuery<Long> countQuery = jpaQueryFactory.select(product.count()).from(product);

		if (brandId != null) {
			BrandId brand = new BrandId(brandId);
			countQuery.where(product.brandId.eq(brand));
		}

		return countQuery;
	}

	private List<ProductInfo> getProductInfosByIds(List<Long> productIds) {
		// ID 순서를 보장하기 위한 Map 생성
		List<ProductInfo> allProductInfos = jpaQueryFactory.select(
				new QProductInfo(
					product.id,
					product.name.name,
					product.price.price,
					product.quantity.quantity,
					brand.brandName.brandName,
					productAggregate.likeCount.likeCount.coalesce(0L)
				)
			)
			.from(product)
			.leftJoin(brand).on(product.brandId.brandId.eq(brand.id))
			.leftJoin(productAggregate).on(productAggregate.productId.productId.eq(product.id))
			.where(product.id.in(productIds))
			.fetch();

		// ID 순서에 맞게 정렬하여 반환
		return productIds.stream()
			.map(id -> allProductInfos.stream()
				.filter(info -> info.productId().equals(id))
				.findFirst()
				.orElse(null))
			.filter(java.util.Objects::nonNull)
			.toList();
	}

	private void applyOrderByForIdQuery(JPAQuery<Long> query, Pageable pageable) {
		pageable.getSort().stream().map(this::createOrderSpecifierForIdQuery).forEach(query::orderBy);
	}

	private OrderSpecifier<?> createOrderSpecifierForIdQuery(Sort.Order order) {
		Order direction = order.isAscending() ? Order.ASC : Order.DESC;

		return switch (order.getProperty()) {
			case "price" -> new OrderSpecifier<>(direction, product.price.price);
			case "likesCount" -> new OrderSpecifier<>(direction, productAggregate.likeCount.likeCount);
			default -> new OrderSpecifier<>(direction, product.createdAt);
		};
	}

}
