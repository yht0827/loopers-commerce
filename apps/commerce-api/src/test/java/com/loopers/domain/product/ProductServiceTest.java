package com.loopers.domain.product;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.loopers.domain.common.BrandId;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	@DisplayName("상품 목록 조회 시, Repository로부터 받은 Product 페이지를 ProductInfo 페이지로 변환하여 반환한다.")
	void getProductList_success() {
		// given
		BrandId brandId = new BrandId(1L);
		PageRequest pageable = PageRequest.of(0, 10);
		List<Product> products = List.of(Product.builder().build());
		Page<Product> productPage = new PageImpl<>(products, pageable, 1);

		when(productRepository.getProductList(brandId, pageable)).thenReturn(productPage);

		// when
		Page<ProductInfo> result = productService.getProductList(brandId, pageable);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getTotalElements()).isEqualTo(1);
	}

	@Test
	@DisplayName("상품 상세 조회 시, 존재하는 ID면 ProductInfo를 반환한다.")
	void getProductDetail_success() {
		// given
		Long productId = 1L;
		Product product = Product.builder().brandId(new BrandId(1L)).build();
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// when
		ProductInfo result = productService.getProductDetail(productId);

		// then
		assertThat(result).isNotNull();
		assertThat(result.brandId()).isEqualTo(product.getBrandId());
	}

	@Test
	@DisplayName("상품 상세 조회 시, 존재하지 않는 ID면 예외를 발생시킨다.")
	void getProductDetail_notFound_throwsException() {
		// given
		Long nonExistentId = 99L;
		when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

		// when & then
		assertThrows(IllegalArgumentException.class, () -> productService.getProductDetail(nonExistentId));
	}
}
