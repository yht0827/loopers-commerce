package com.loopers.application.ranking;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.rank.RankingItem;
import com.loopers.domain.rank.RankingPeriod;
import com.loopers.domain.rank.RankingReadService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RankingService implements RankingUseCase {

	private final RankingReadService rankingReadService;
	private final ProductService productService;

	@Transactional(readOnly = true)
	@Override
	public RankingPageResult getRanking(final GetRankingQuery query) {
		RankingPeriod period = RankingPeriod.from(query.period());
		return switch (period) {
			case DAILY -> getDaily(query);
			case WEEKLY -> getWeekly(query);
			case MONTHLY -> getMonthly(query);
		};
	}

	public RankingPageResult getDaily(final GetRankingQuery query) {
		Page<RankingItem> page = rankingReadService.getRanking(query.date(), query.pageable());

		if (page.isEmpty()) {
			return RankingPageResult.from(page, List.of());
		}

		// 상품 ID 추출
		List<Long> ids = page.getContent().stream().map(RankingItem::getProductId).toList();

		// 상품 정보 배치 조회
		Map<Long, ProductInfo> productMap = productService.getProductByIds(ids);

		List<RankingProductResult> items = page.getContent().stream()
			.filter(item -> productMap.containsKey(item.getProductId()))
			.map(item -> RankingProductResult.from(item, productMap.get(item.getProductId())))
			.toList();

		return RankingPageResult.from(page, items);
	}

	public RankingPageResult getWeekly(GetRankingQuery query) {
		return null;
	}

	public RankingPageResult getMonthly(GetRankingQuery query) {
		return null;
	}

}
