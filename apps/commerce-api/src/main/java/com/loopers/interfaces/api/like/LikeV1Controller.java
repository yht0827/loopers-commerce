package com.loopers.interfaces.api.like;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.like.LikeFacade;
import com.loopers.application.like.LikeResult;
import com.loopers.interfaces.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeV1Controller {
	private final LikeFacade likeFacade;

	@PostMapping("/products/{productId}")
	public ApiResponse<LikeResponse> likeProduct(
		@PathVariable Long productId, @RequestHeader final Long userId) {
		LikeResult likeResult = likeFacade.likeProduct(userId, productId);
		LikeResponse response = LikeResponse.from(likeResult);
		return ApiResponse.success(response);
	}

	@DeleteMapping("/products/{productId}")
	public ApiResponse<Void> unlikeProduct(
		@PathVariable Long productId, @RequestHeader final Long userId) {
		likeFacade.unlikeProduct(userId, productId);
		return ApiResponse.success(null);
	}

	@GetMapping("/products")
	public ApiResponse<List<LikeResponse>> getLikedProductList(@RequestHeader final Long userId) {
		List<LikeResult> likedProductList = likeFacade.getLikedProductList(userId);

		List<LikeResponse> response = likedProductList.stream()
			.map(LikeResponse::from)
			.toList();

		return ApiResponse.success(response);
	}

}
