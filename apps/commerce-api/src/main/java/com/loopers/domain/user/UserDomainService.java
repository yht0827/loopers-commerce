package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDomainService {
	private final UserValidator validator;
	private final UserRepository userRepository;

	public User createUser(final String userId, String email, String birthday, String gender) {

		User user = User.create()
			.userId(UserId.of(userId))
			.email(Email.of(email))
			.birthday(Birthday.of(birthday))
			.gender(Gender.of(gender))
			.build();

		validator.validateForCreation(user);

		try {
			return userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new CoreException(BAD_REQUEST, USER_ID_ALREADY_EXISTS.getMessage());
		}
	}
}
