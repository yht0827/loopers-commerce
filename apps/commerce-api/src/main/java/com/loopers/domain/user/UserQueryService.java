package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserQueryService {
	private final UserRepository userRepository;

	public User getUser(final String userId) {
		return userRepository.findByUserId(userId)
			.orElseThrow(() -> new CoreException(NOT_FOUND, USER_NOT_FOUND.format(userId)));
	}
}
