package com.loopers.infrastructure;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.EventHandled;
import com.loopers.domain.EventHandledRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventHandledRepositoryImpl implements EventHandledRepository {

	private final EventHandledJpaRepository eventHandledJpaRepository;

	@Override
	public EventHandled save(EventHandled eventHandled) {
		return eventHandledJpaRepository.save(eventHandled);
	}

	@Override
	public void saveAll(List<EventHandled> eventHandledList) {
		eventHandledJpaRepository.saveAll(eventHandledList);
	}

	@Override
	public boolean existsByEventIdAndConsumerGroup(String eventId, String consumerGroup) {
		return eventHandledJpaRepository.existsByEventIdAndConsumerGroup(eventId, consumerGroup);
	}
	
	@Override
	public Optional<EventHandled> findByEventIdAndConsumerGroup(String eventId, String consumerGroup) {
		return eventHandledJpaRepository.findByEventIdAndConsumerGroup(eventId, consumerGroup);
	}
	
	@Override
	public boolean isNewerVersion(String eventId, String consumerGroup, Long version, LocalDateTime processedAt) {
		Optional<EventHandled> existing = findByEventIdAndConsumerGroup(eventId, consumerGroup);
		
		if (existing.isEmpty()) {
			return true; // 처음 처리하는 이벤트
		}
		
		EventHandled handled = existing.get();
		
		// 버전 비교 (버전이 있는 경우)
		if (version != null && handled.getVersion() != null) {
			return version > handled.getVersion();
		}
		
		// 시간 비교 (버전이 없거나 같은 경우)
		if (processedAt != null && handled.getLastProcessedAt() != null) {
			return processedAt.isAfter(handled.getLastProcessedAt());
		}
		
		return false; // 기존 것이 더 최신이거나 비교할 수 없는 경우
	}
}
