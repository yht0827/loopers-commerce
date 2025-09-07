package com.loopers.application.user;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.UserValidator;
import com.loopers.support.error.CoreException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserApplicationService implements UserUseCase {
	private final UserRepository userRepository;
	private final UserValidator userValidator;

	@Override
	public UserResult createUser(final CreateUserCommand userCommand) {
		final User user = userCommand.toEntity();
		userValidator.validateUniqueUserId(user.getUserId());
		try {
			User savedUser = userRepository.save(user);
			return UserResult.from(savedUser);
		} catch (DataIntegrityViolationException e) {
			throw new CoreException(BAD_REQUEST, USER_ID_ALREADY_EXISTS.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserResult getUser(final GetUserQuery query) {
		User user = userRepository.findByUserId(query.userId())
			.orElseThrow(() -> new CoreException(NOT_FOUND, USER_NOT_FOUND.format(query.userId())));
		return UserResult.from(user);
	}

}
