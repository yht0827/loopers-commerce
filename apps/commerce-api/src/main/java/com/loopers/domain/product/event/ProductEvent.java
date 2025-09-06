package com.loopers.domain.product.event;

import com.loopers.support.event.Event;

public interface ProductEvent extends Event {
	Long getProductId();
}
