package com.loopers.domain;

import java.util.List;

public interface EventLogRepository {

	void save(EventLog eventLog);
	
	void saveAll(List<EventLog> eventLogs);
}
