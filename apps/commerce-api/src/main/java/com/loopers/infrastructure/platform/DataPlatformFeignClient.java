package com.loopers.infrastructure.platform;

import org.springframework.stereotype.Component;

import com.loopers.domain.platform.DataPlatformGateway;
import com.loopers.domain.platform.event.DataPlatformEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataPlatformFeignClient implements DataPlatformGateway {

	@Override
	public void send(DataPlatformEvent event) {
		log.info("데이터 플랫폼 전송 - DataType: {}, AggregateId: {}", event.dataType(), event.aggregateId());
	}
}
