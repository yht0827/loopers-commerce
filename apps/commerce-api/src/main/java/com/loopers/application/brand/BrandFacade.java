package com.loopers.application.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class BrandFacade {
	private final BrandService brandService;
	private final ProductService productService;

	public BrandResult getBrandById(final Long brandId, final Pageable pageable) {
		BrandInfo brandInfo = brandService.getBrandById(brandId);
		Page<ProductInfo> productInfos = productService.getProductsByBrandId(brandId, pageable);

		return BrandResult.from(brandInfo, productInfos);
	}
}
