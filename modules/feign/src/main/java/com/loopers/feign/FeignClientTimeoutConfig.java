package com.loopers.feign;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Request;
import feign.Retryer;

@Configuration
public class FeignClientTimeoutConfig {

	/**
	 * 공통 FeignClient Timeout 정책 설정
	 *
	 */
	@Bean
	public Request.Options feignRequestOptions() {
		return new Request.Options(
			5000,
			TimeUnit.MILLISECONDS,
			5000,
			TimeUnit.MILLISECONDS,
			true
		);
	}

	@Bean
	public Retryer feignRetryer() {
		return new Retryer.Default(100L, 1000L, 3);
	}
}
