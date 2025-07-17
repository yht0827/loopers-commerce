package com.loopers.domain.users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.application.users.port.in.UsersCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsersService {
	private final UsersRepository usersRepository;

	@Transactional
	public UsersModel join(final UsersCommand usersCommand) {
		UsersModel entity = usersCommand.toEntity();
		return usersRepository.save(entity);
	}

	@Transactional(readOnly = true)
	public UsersModel me() {

		return null;
	}
}
