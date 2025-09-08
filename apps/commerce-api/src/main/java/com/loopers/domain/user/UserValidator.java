package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator {
	private final UserRepository userRepository;

	public void validateUniqueUserId(final UserId userId) {
		boolean isExisted = userRepository.existsByUserId(userId);
		if (isExisted) {
			throw new CoreException(BAD_REQUEST, USER_ID_ALREADY_EXISTS.getMessage());
		}
	}

	public void validateUniqueEmail(final Email email) {
		boolean isExisted = userRepository.existsByEmail(email);
		if (isExisted) {
			throw new CoreException(BAD_REQUEST, EMAIL_ALREADY_EXISTS.getMessage());
		}
	}

	public void validateForCreation(final User user) {
		validateUniqueUserId(user.getUserId());
		validateUniqueEmail(user.getEmail());
	}
}
