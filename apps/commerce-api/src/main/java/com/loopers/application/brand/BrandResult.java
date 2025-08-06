package com.loopers.application.brand;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.brand.BrandName;

public record BrandResult(Long brandId, BrandName brandName) {

	public static BrandResult from(BrandInfo brand) {
		return new BrandResult(brand.brandId(), brand.brandName());
	}
}
