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

	public <T extends Event> void publish(T event) {
		try {
			Envelope<T> envelope = Envelope.of(event);
			springEventPublisher.publishEvent(envelope);
			log.debug("이벤트 발행 성공: {}", event.getClass().getSimpleName());
		} catch (Exception e) {
			log.error("이벤트 발행 실패: {}", event.getClass().getSimpleName(), e);
			throw e;
		}
	}
}
