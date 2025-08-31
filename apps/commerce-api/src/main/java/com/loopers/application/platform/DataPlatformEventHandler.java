package com.loopers.application.platform;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.platform.DataPlatformService;
import com.loopers.domain.platform.event.DataPlatformEvent;
import com.loopers.support.event.DomainApplicationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataPlatformEventHandler {

	private final DataPlatformService dataPlatformService;

	@EventListener
	public void handleDataPlatformSync(DomainApplicationEvent event) {
		if (!event.isPayloadOfType(DataPlatformEvent.class)) {
			return;
		}

		DataPlatformEvent dataPlatformEvent = event.getPayload(DataPlatformEvent.class);
		processDataPlatformSync(dataPlatformEvent);
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void processDataPlatformSync(DataPlatformEvent event) {
		try {

			dataPlatformService.sendData(
				event.dataType(),
				event.aggregateId(),
				event.payload(),
				event.source()
			);

			log.info("데이터 플랫폼 전송 완료: {} - {}", event.dataType(), event.aggregateId());

		} catch (Exception e) {
			log.error("데이터 플랫폼 전송 실패: {} - {}", event.dataType(), event.aggregateId(), e);
		}
	}
}
