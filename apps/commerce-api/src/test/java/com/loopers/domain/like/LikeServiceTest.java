package com.loopers.domain.like;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.loopers.domain.common.BrandId;
import com.loopers.domain.common.Price;
import com.loopers.domain.common.Quantity;
import com.loopers.domain.product.LikeCount;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductName;
import com.loopers.domain.product.ProductRepository;
import com.loopers.support.error.CoreException;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

	@Mock
	private LikeRepository likeRepository;
	@Mock
	private ProductRepository productRepository;
	@InjectMocks
	private LikeService likeService;

	@Test
	@DisplayName("좋아요 등록 시, 성공적으로 처리된다.")
	void likeProduct_success() {
		// given
		Long userId = 1L;
		Long productId = 1L;
		LikeId likeId = new LikeId(userId, productId);
		Like like = new Like(likeId);
		Product product = Product.builder()
			.name(new ProductName("테스트 상품"))
			.price(new Price(10000L))
			.likeCount(new LikeCount(6L)) // 초기 좋아요 수 6개
			.quantity(new Quantity(10L)) // 초기 재고 10개
			.build();

		when(likeRepository.findById(likeId.userId())).thenReturn(Optional.of(like)); // 아직 좋아요를 안 누름
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// when
		likeService.likeProduct(userId, productId);

		// then
		verify(likeRepository, times(1)).save(any(Like.class));

	}

	@Test
	@DisplayName("이미 좋아요를 누른 상품에 다시 요청하면 예외가 발생한다.")
	void likeProduct_alreadyLiked_throwsException() {
		// given
		LikeId likeId = new LikeId(1L, 3L);
		Like like = new Like(likeId);

		when(likeRepository.findById(likeId.userId())).thenReturn(Optional.of(like)); // 이미 좋아요 누름

		// when and then
		assertThatThrownBy(() -> likeService.likeProduct(likeId.userId(), likeId.targetId())).isInstanceOf(CoreException.class).hasMessage("이미 좋아요를 누른 상품입니다.");
	}

	@Test
	@DisplayName("좋아요 취소 시, 성공적으로 처리된다.")
	void unlikeProduct_success() {
		// given
		LikeId likeId = new LikeId(1L, 4L);
		Like like = new Like(likeId);
		Product mockProduct = new Product(new BrandId(1L), new ProductName("테스트 상품"), new Price(10000L), new LikeCount(10L), new Quantity(6L));

		when(likeRepository.findById(likeId.userId())).thenReturn(Optional.of(like));
		when(productRepository.findById(likeId.targetId())).thenReturn(Optional.of(mockProduct));

		// when
		likeService.unlikeProduct(likeId.userId(), likeId.targetId());

		// then
		verify(likeRepository, times(1)).delete(like); // delete 메서드 호출 검증
	}
}
