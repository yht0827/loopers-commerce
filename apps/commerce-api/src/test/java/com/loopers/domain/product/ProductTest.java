package com.loopers.domain.product;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.domain.common.Price;
import com.loopers.domain.common.Quantity;
import com.loopers.support.error.CoreException;

public class ProductTest {

	@DisplayName("상품 모델에 대해")
	@Nested
	class ProductLogic {
		private Product product;

		@BeforeEach
		void setUp() {
			// 각 테스트 전에 공통으로 사용할 상품 객체 생성
			product = Product.builder()
				.name(new ProductName("테스트 상품"))
				.price(new Price(10000L))
				.likeCount(new LikeCount(6L)) // 초기 좋아요 수 6개
				.quantity(new Quantity(10L)) // 초기 재고 10개
				.build();
		}

		@DisplayName("생성 시 필드 값들이 주어지면, 정상적으로 생성된다.")
		@Test
		void createProduct_success() {
			// assert
			assertAll(
				() -> assertThat(product.getName().name()).isEqualTo("테스트 상품"),
				() -> assertThat(product.getPrice().price()).isEqualTo(10000L),
				() -> assertThat(product.getQuantity().quantity()).isEqualTo(10L)
			);
		}

		@DisplayName("재고 확인 시, 요청 수량보다 재고가 충분하면, true를 반환한다.")
		@Test
		void checkStock_whenSufficient() {
			// arrange
			long orderQuantity = 5L;

			// act and assert
			boolean sufficient = product.getQuantity().isSufficient(new Quantity(orderQuantity));
			assertThat(sufficient).isTrue();
		}

		@DisplayName("재고 확인 시, 요청 수량보다 재고가 부족하면, false를 반환한다.")
		@Test
		void checkStock_whenInsufficient() {
			// arrange
			long orderQuantity = 15L; // 재고(10)보다 많은 수량

			// act and assert
			boolean insufficient = product.getQuantity().isSufficient(new Quantity(orderQuantity));
			assertThat(insufficient).isFalse();
		}

		@DisplayName("재고 감소 시, 재고가 충분하면, 수량이 정상적으로 감소한다.")
		@Test
		void decreaseStock_whenSufficient() {
			// arrange
			long decreaseAmount = 3L;

			// act
			product.decreaseStock(new Quantity(decreaseAmount));

			// assert
			Long quantity = product.getQuantity().quantity();
			assertThat(quantity).isEqualTo(7L);
		}

		@DisplayName("좋아요 수를 업데이트하면, 정상적으로 반영된다.")
		@Test
		void updateLikeCount_success() {
			// arrange
			// 현재 좋아요 수(6)에서 1 증가시킨 LikeCount 객체 생성
			LikeCount newLikeCount = product.getLikeCount().increase(); // newLikeCount.likeCount()는 7L

			// act
			product.updateLikeCount(newLikeCount);

			// assert
			assertThat(product.getLikeCount().likeCount()).isEqualTo(7L);
		}

		@DisplayName("재고 감소 시, 재고가 부족하면, 예외가 발생한다.")
		@Test
		void decreaseStock_whenInsufficient_throwsException() {
			// arrange
			long orderQuantity = 15L; // 재고(10)보다 많은 수량

			// act and assert
			assertThatThrownBy(() -> product.decreaseStock(new Quantity(orderQuantity)))
				.isInstanceOf(CoreException.class)
				.hasMessage("재고가 충분하지 않습니다.");
		}

		@DisplayName("생성 시, 상품 이름이 null이면, 예외가 발생한다.")
		@Test
		void createProduct_withNullName_throwsException() {
			// assert
			assertThatThrownBy(() -> new ProductName(null))
				.isInstanceOf(CoreException.class)
				.hasMessage("상품 이름은 비어있을 수 없습니다.");
		}

		@DisplayName("생성 시, 가격이 0보다 작으면, 예외가 발생한다.")
		@Test
		void createProduct_withNegativePrice_throwsException() {
			// assert
			assertThatThrownBy(() -> new Price(-100L))
				.isInstanceOf(CoreException.class)
				.hasMessage("가격은 0 이상이어야 합니다.");
		}
	}
}
