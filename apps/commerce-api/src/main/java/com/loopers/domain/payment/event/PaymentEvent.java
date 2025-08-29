package com.loopers.domain.payment.event;

import com.loopers.support.event.Event;

public interface PaymentEvent extends Event {
	String getOrderId();
}
