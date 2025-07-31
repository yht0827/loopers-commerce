package com.loopers.interfaces.api.api.brand;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.brand.BrandFacade;
import com.loopers.application.brand.BrandResult;
import com.loopers.interfaces.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/brand")
public class BrandV1Controller {
	private final BrandFacade brandFacade;

	@GetMapping("/{brandId}")
	public ApiResponse<BrandResponse> getBrandById(@PathVariable final Long brandId, final Pageable pageable) {
		BrandResult brandResult = brandFacade.getBrandById(brandId, pageable);
		BrandResponse brandResponse = BrandResponse.from(brandResult);
		return ApiResponse.success(brandResponse);
	}
}
