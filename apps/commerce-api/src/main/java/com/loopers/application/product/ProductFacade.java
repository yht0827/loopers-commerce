package com.loopers.application.product;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ProductFacade {

	private final ProductService productService;
	private final BrandService brandService;

	public ProductListResult getProductList(final ProductCriteria.GetProductList criteria) {
		ProductCommand.GetProductList command = criteria.toCommand();
		Page<ProductInfo> products = productService.getProductList(command);
		return ProductListResult.from(products);
	}

	public ProductDetailResult getProductDetail(final Long productId) {
		ProductInfo productInfo = productService.getProductDetail(productId);
		BrandInfo brandInfo = brandService.getBrandById(productInfo.brandId());

		return ProductDetailResult.from(productInfo, brandInfo);
	}

}
