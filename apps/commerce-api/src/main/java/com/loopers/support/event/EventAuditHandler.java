package com.loopers.support.event;

import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.event.EventAuditLog;
import com.loopers.domain.event.EventAuditLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventAuditHandler {

	private final EventAuditLogRepository eventAuditLogRepository;

	@EventListener
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void auditAllEvents(DomainApplicationEvent event) {
		log.info("=== 이벤트 감사 ===");
		log.info("ID: {}", event.getEventId());
		log.info("타입: {}", event.getEventType());
		log.info("애그리게이트 ID: {}", event.getAggregateId());
		log.info("코릴레이션 ID: {}", event.getCorrelationId());
		log.info("발생시간: {}", event.getOccurredAt());
		log.info("==================");

		EventAuditLog auditLog = EventAuditLog.from(event);
		eventAuditLogRepository.save(auditLog);
	}
}
