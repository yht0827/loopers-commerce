package com.loopers.interfaces.api.order;

import java.util.List;

public record OrderRequest(
	List<OrderItemRequest> items
) {

	public List<OrderItemRequest> toCommand() {
		return items;
	}
}
