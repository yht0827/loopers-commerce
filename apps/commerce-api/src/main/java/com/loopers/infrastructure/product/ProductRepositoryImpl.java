package com.loopers.infrastructure.product;

import static com.loopers.domain.product.QProduct.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

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
	public Page<Product> getProductList(final Long brandId, final Pageable pageable) {
		JPAQuery<Product> query = jpaQueryFactory.selectFrom(product)
			.where(product.brandId.eq(brandId));

		pageable.getSort().stream().forEach(order -> {
			String property = order.getProperty();
			Order direction = order.isAscending() ? Order.ASC : Order.DESC;

			OrderSpecifier<?> orderSpecifier = switch (property) {
				case "createdAt" -> new OrderSpecifier<>(direction, product.createdAt);
				case "price" -> new OrderSpecifier<>(direction, product.price.price);
				case "likesCount" -> new OrderSpecifier<>(direction, product.likeCount.likeCount);
				default -> new OrderSpecifier<>(direction, product.createdAt); // 기본 정렬
			};

			query.orderBy(orderSpecifier);
		});

		List<Product> products = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> count = jpaQueryFactory.select(product.count())
			.from(product)
			.where(product.brandId.eq(brandId));

		return PageableExecutionUtils.getPage(products, pageable, count::fetchOne);
	}

}
