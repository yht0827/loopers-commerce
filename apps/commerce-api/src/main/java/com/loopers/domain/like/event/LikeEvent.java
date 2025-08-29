package com.loopers.domain.like.event;

import com.loopers.support.event.Event;

public interface LikeEvent extends Event {
	String getUserId();
}
