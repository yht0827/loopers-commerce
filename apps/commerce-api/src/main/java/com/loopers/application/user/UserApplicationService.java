package com.loopers.application.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserCommandService;
import com.loopers.domain.user.UserQueryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserApplicationService {
	private final UserCommandService userCommandService;
	private final UserQueryService userQueryService;

	@Transactional
	public UserInfo createUser(final UserCommand.CreateUser userCommand) {
		User user = userCommand.toEntity();
		User savedUser = userCommandService.createUser(user);
		return UserInfo.from(savedUser);
	}

	@Transactional(readOnly = true)
	public UserInfo getUser(final UserQuery.GetUser query) {
		final String userId = query.userId();
		User user = userQueryService.getUser(userId);
		return UserInfo.from(user);
	}

}
