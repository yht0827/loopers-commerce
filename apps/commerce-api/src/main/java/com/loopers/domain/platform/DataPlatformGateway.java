package com.loopers.domain.platform;

import com.loopers.domain.platform.event.DataPlatformEvent;

public interface DataPlatformGateway {

	void send(DataPlatformEvent event);

}
