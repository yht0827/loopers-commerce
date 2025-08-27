package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import org.springframework.stereotype.Service;

import com.loopers.domain.common.UserId;
import com.loopers.support.error.CoreException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserValidator {
	private final UserRepository userRepository;

	public void validateUniqueUserId(final UserId userId) {
		boolean isExisted = userRepository.existsByUserId(userId.userId());
		if (isExisted) {
			throw new CoreException(BAD_REQUEST, USER_ID_ALREADY_EXISTS.getMessage());
		}
	}
}
