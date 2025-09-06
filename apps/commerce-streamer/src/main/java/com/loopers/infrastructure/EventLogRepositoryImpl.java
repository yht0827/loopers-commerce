package com.loopers.infrastructure;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.loopers.domain.EventLog;
import com.loopers.domain.EventLogRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventLogRepositoryImpl implements EventLogRepository {

	private final EventLogJpaRepository eventLogJpaRepository;

	@Override
	public void save(EventLog eventLog) {
		eventLogJpaRepository.save(eventLog);
	}

	@Override
	public void saveAll(List<EventLog> eventLogs) {
		eventLogJpaRepository.saveAll(eventLogs);
	}
}
