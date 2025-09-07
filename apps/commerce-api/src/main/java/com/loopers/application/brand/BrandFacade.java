package com.loopers.application.brand;

import org.springframework.stereotype.Component;

import com.loopers.domain.brand.BrandId;
import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.brand.BrandService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class BrandFacade {
	private final BrandService brandService;

	public BrandResult getBrandById(final BrandId brandId) {
		BrandInfo brandInfo = brandService.getBrandById(brandId.getBrandId());

		return BrandResult.from(brandInfo);
	}
}
