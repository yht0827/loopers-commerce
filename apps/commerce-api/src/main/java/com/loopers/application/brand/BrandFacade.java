package com.loopers.application.brand;

import org.springframework.stereotype.Component;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.common.BrandId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class BrandFacade {
	private final BrandService brandService;

	public BrandResult getBrandById(final BrandId brandId) {
		BrandInfo brandInfo = brandService.getBrandById(brandId);

		return BrandResult.from(brandInfo);
	}
}
