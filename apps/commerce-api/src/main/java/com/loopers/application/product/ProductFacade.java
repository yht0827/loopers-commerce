package com.loopers.application.product;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.rank.RankingReadService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ProductFacade {

	private final ProductService productService;
	private final RankingReadService rankingReadService;

	public ProductListResult getProductList(final ProductCriteria.GetProductList criteria) {
		ProductCommand.GetProductList command = criteria.toCommand();
		Page<ProductInfo> products = productService.getProductList(command);
		return ProductListResult.from(products);
	}

	public ProductDetailResult getProductDetail(final Long productId) {
		ProductInfo productInfo = productService.getProductDetail(productId);

		Long rank = rankingReadService.getProductRanking(productId);

		return ProductDetailResult.from(productInfo, rank);
	}

}
