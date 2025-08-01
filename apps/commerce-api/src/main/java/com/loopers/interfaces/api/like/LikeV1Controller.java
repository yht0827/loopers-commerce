package com.loopers.interfaces.api.like;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.like.LikeFacade;
import com.loopers.application.like.LikeResult;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.util.UserIdentifier;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeV1Controller {
	private final LikeFacade likeFacade;

	@PostMapping("/products/{productId}")
	public ApiResponse<LikeResponse> likeProduct(
		@PathVariable Long productId, HttpServletRequest servletRequest) {
		Long userId = UserIdentifier.getUserId(servletRequest);
		LikeResult likeResult = likeFacade.likeProduct(userId, productId);
		LikeResponse response = LikeResponse.from(likeResult);
		return ApiResponse.success(response);
	}

	@DeleteMapping("/products/{productId}")
	public ApiResponse<Void> unlikeProduct(
		@PathVariable Long productId, HttpServletRequest servletRequest) {
		Long userId = UserIdentifier.getUserId(servletRequest);
		likeFacade.unlikeProduct(userId, productId);
		return ApiResponse.success(null);
	}

	@GetMapping("/products")
	public ApiResponse<List<LikeResponse>> getLikedProductList(HttpServletRequest servletRequest) {
		Long userId = UserIdentifier.getUserId(servletRequest);
		List<LikeResult> likedProductList = likeFacade.getLikedProductList(userId);

		List<LikeResponse> response = likedProductList.stream()
			.map(LikeResponse::from)
			.toList();

		return ApiResponse.success(response);
	}

}
