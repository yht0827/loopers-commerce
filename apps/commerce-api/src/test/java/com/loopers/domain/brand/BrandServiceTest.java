package com.loopers.domain.brand;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.loopers.domain.common.BrandId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@ExtendWith(MockitoExtension.class) // Mockito 확장 기능을 사용합니다.
public class BrandServiceTest {

	@Mock
	private BrandRepository brandRepository;

	@InjectMocks
	private BrandService brandService;

	@Test
	@DisplayName("브랜드 ID로 조회 성공")
	void getBrandById_success() {
		// given
		BrandId brandId = new BrandId(1L);
		Brand brand = new Brand(new BrandName("나이키"));
		BrandInfo expectedBrandInfo = BrandInfo.from(brand);

		when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));

		// when
		BrandInfo actualBrandInfo = brandService.getBrandById(brandId);

		// then
		assertThat(actualBrandInfo).isNotNull();
		assertThat(actualBrandInfo.brandId()).isEqualTo(expectedBrandInfo.brandId());
		assertThat(actualBrandInfo.brandName()).isEqualTo(expectedBrandInfo.brandName());

		verify(brandRepository, times(1)).findById(brandId);
	}

	@Test
	@DisplayName("존재하지 않는 브랜드 ID로 조회 시 예외 발생")
	void getBrandById_notFound_throwsException() {
		// given - 테스트 준비
		BrandId nonExistentBrandId = new BrandId(99L);

		when(brandRepository.findById(nonExistentBrandId)).thenReturn(Optional.empty());

		// when and then
		CoreException exception = assertThrows(CoreException.class, () -> brandService.getBrandById(nonExistentBrandId));

		assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
		assertThat(exception.getMessage()).isEqualTo("해당 [id = " + nonExistentBrandId + "]의 브랜드를 찾을 수 없습니다.");

		verify(brandRepository, times(1)).findById(nonExistentBrandId);
	}

}
