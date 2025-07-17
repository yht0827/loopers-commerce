package com.loopers.application.users.facade;

import org.springframework.stereotype.Component;

import com.loopers.application.users.port.in.UsersCommand;
import com.loopers.application.users.port.out.UsersInfo;
import com.loopers.domain.users.UsersModel;
import com.loopers.domain.users.UsersService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UsersFacade {
	private final UsersService usersService;

	public UsersInfo join(final UsersCommand usersCommand) {
		UsersModel usersModel = usersService.join(usersCommand);
		return UsersInfo.from(usersModel);
	}

	public UsersInfo me() {
		usersService.me();

		return null;
	}

}
