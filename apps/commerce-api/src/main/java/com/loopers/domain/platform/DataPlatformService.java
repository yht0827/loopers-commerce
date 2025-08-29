package com.loopers.domain.platform;

public interface DataPlatformService {
	void sendData(String dataType, String aggregateId, String payload, String source);
}