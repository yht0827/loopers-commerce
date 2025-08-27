package com.loopers.application.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserCommandService;
import com.loopers.domain.user.UserQueryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserApplicationService {
	private final UserCommandService userCommandService;
	private final UserQueryService userQueryService;

	public UserInfo createUser(final UserCommand.CreateUser userCommand) {
		final User user = userCommand.toEntity();
		User savedUser = userCommandService.createUser(user);
		return UserInfo.from(savedUser);
	}

	@Transactional(readOnly = true)
	public UserInfo getUser(final UserQuery.GetUser query) {
		User user = userQueryService.getUser(query.userId());
		return UserInfo.from(user);
	}

}
