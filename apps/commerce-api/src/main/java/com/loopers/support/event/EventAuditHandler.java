package com.loopers.support.event;

import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventAuditHandler {

	@EventListener
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public <T extends Event> void auditAllEvents(Envelope<T> event) {
		log.info("이벤트 발생 - Type: {}, AggregateId: {}, EventId: {}, CorrelationId: {}",
			event.getEventType(), event.getAggregateId(), event.getEventId(), event.getCorrelationId());
	}
}
