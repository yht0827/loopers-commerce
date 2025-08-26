package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserCommandService {
	private final UserRepository userRepository;

	public User createUser(final User user) {
		final String userId = user.getUserId().userId();

		boolean isExisted = userRepository.existsByUserId(userId);
		if (isExisted) {
			throw new CoreException(BAD_REQUEST, USER_ID_ALREADY_EXISTS.getMessage());
		}

		return userRepository.save(user);
	}
}
