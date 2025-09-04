package com.loopers.application.platform;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.platform.DataPlatformGateway;
import com.loopers.domain.platform.event.DataPlatformEvent;
import com.loopers.support.event.Envelope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataPlatformEventHandler {

	private final DataPlatformGateway dataPlatformGateway;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleDataPlatformEvent(Envelope<DataPlatformEvent> event) {
		if (!DataPlatformEvent.EVENT_TYPE.equals(event.getEventType())) {
			return;
		}
		try {
			dataPlatformGateway.send(event.getPayload());
		} catch (Exception e) {
			log.error("데이터 플랫폼 전송 실패: {}", event.getPayload().aggregateId(), e);
		}
	}
}
