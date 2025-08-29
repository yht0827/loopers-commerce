package com.loopers.domain.order.event;

import com.loopers.support.event.Event;

public interface OrderEvent extends Event {
	String getOrderId();
}
