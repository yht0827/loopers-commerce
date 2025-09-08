package com.loopers.application.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserDomainService;
import com.loopers.domain.user.UserFinder;
import com.loopers.domain.user.UserId;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserUseCase {
	private final UserDomainService userDomainService;
	private final UserFinder userFinder;

	@Override
	public UserResult createUser(final CreateUserCommand command) {
		User savedUser = userDomainService.createUser(
			command.userId(),
			command.email(),
			command.birthday(),
			command.gender());

		return UserResult.from(savedUser);
	}

	@Override
	@Transactional(readOnly = true)
	public UserResult getUser(final GetUserQuery query) {
		final UserId userId = UserId.of(query.userId());

		User user = userFinder.findByUserId(userId);

		return UserResult.from(user);
	}

}
