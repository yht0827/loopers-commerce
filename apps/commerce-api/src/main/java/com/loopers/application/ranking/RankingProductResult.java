package com.loopers.application.ranking;

import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.rank.RankingItem;

public record RankingProductResult(
	Long rank, Double score,
	Long productId, String productName, Long price, Long quantity,
	String brandName, Long likeCount
) {
	public static RankingProductResult from(final RankingItem item, final ProductInfo info) {
		return new RankingProductResult(
			item.getRank(), item.getScore(), item.getProductId(),
			info.productName(), info.price(), info.quantity(),
			info.brandName(), info.likeCount()
		);
	}
}
