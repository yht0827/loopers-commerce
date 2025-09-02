package com.loopers.domain.product.event;

import com.loopers.support.event.Event;

public interface LikeEvent extends Event {
	String getUserId();
}
