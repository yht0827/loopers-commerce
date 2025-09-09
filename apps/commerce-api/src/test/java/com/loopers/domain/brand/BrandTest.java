package com.loopers.domain.brand;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.support.error.CoreException;

public class BrandTest {

	@DisplayName("브랜드 모델을 생성할 때, ")
	@Nested
	class Create {
		@DisplayName("필드 값들이 모두 주어지면, 정상적으로 생성된다.")
		@Test
		void createsBrandModel_whenFieldsAreProvided() {
			// arrange (준비)
			String name = "나이키";

			// act (실행)
			Brand brand = new Brand(new BrandName(name));

			// assert (검증)
			assertAll(
				() -> assertThat(brand.getBrandName().getBrandName()).isEqualTo(name)
			);
		}

		@DisplayName("이름이 null이거나 공백이면, 예외가 발생한다.")
		@Test
		void createBrand_withBlankName_throwsException() {
			// assert
			assertThatThrownBy(() -> new BrandName(null))
				.isInstanceOf(CoreException.class)
				.hasMessage("브랜드 이름은 비어있을 수 없습니다.");

			assertThatThrownBy(() -> new BrandName("  "))
				.isInstanceOf(CoreException.class)
				.hasMessage("브랜드 이름은 비어있을 수 없습니다.");
		}
	}
}

