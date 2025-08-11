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
		JPAQuery<Product> query = jpaQueryFactory.selectFrom(product);
		JPAQuery<Long> countQuery = jpaQueryFactory.select(product.count()).from(product);

		if (brandId != null) {
			BrandId brand = new BrandId(brandId);
			query.where(product.brandId.eq(brand));
			countQuery.where(product.brandId.eq(brand));
		}

		applyOrderBy(query, pageable);

		List<Product> products = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return PageableExecutionUtils.getPage(products, pageable, countQuery::fetchOne);
	}

	private void applyOrderBy(JPAQuery<Product> query, Pageable pageable) {
		pageable.getSort().stream()
			.map(this::createOrderSpecifier)
			.forEach(query::orderBy);
	}

	private OrderSpecifier<?> createOrderSpecifier(org.springframework.data.domain.Sort.Order order) {
		Order direction = order.isAscending() ? Order.ASC : Order.DESC;

		return switch (order.getProperty()) {
			case "price" -> new OrderSpecifier<>(direction, product.price.price);
			case "likesCount" -> new OrderSpecifier<>(direction, product.likeCount.likeCount);
			default -> new OrderSpecifier<>(direction, product.createdAt);
		};
	}

}
