package com.loopers.infrastructure.platform;

import org.springframework.stereotype.Service;

import com.loopers.domain.platform.DataPlatformService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataPlatformServiceImpl implements DataPlatformService {

	@Override
	public void sendData(String dataType, String aggregateId, String payload, String source) {

		try {

			// 데이터 플랫폼 전송 (Mock)
			Thread.sleep(2);

			log.info("데이터 플랫폼 전송 성공");

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
