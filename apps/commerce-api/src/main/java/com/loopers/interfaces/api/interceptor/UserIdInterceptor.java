package com.loopers.interfaces.api.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserIdInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		String userId = request.getHeader("X-USER-ID");
		if (userId == null || userId.isBlank()) {
			log.warn("X-USER-ID 헤더가 필요합니다.");
			throw new CoreException(ErrorType.BAD_REQUEST, "X-USER-ID 헤더가 필요합니다.");
		}
		log.info("User ID: {}", userId);
		return true;
	}
}
