package com.loopers.support.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {
	private final ApplicationEventPublisher springEventPublisher;

	public void publish(Event event) {
		publish(this, event);
	}

	public void publish(Object source, Event event) {
		EventEnvelope<Event> envelope = EventEnvelope.of(event);
		DomainApplicationEvent domainApplicationEvent = new DomainApplicationEvent(source, envelope);

		try {
			springEventPublisher.publishEvent(domainApplicationEvent);
		} catch (Exception e) {
			log.error("이벤트 발행 실패: {} [{}]", envelope.getEventType(), envelope.getEventId(), e);
			throw e;
		}
	}
}
