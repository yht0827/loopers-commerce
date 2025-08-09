package com.loopers.domain.brand;

import org.springframework.stereotype.Service;

import com.loopers.domain.common.BrandId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BrandService {
	private final BrandRepository brandRepository;

	public BrandInfo getBrandById(final BrandId brandId) {
		Brand brand = brandRepository.findById(brandId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 [id = " + brandId + "]의 브랜드를 찾을 수 없습니다."));

		return BrandInfo.from(brand);
	}
}
