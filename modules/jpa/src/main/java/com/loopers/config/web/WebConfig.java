package com.loopers.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.loopers.interfaces.api.interceptor.UserIdInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final UserIdInterceptor userIdInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userIdInterceptor)
			.addPathPatterns("/api/**") // 인터셉터를 적용할 URL 패턴을 지정합니다. (예: /api/로 시작하는 모든 경로)
			.excludePathPatterns("/api/public/**", "/login", "/error"); // 인터셉터에서 제외할 URL 패턴을 지정합니다.
	}
}
