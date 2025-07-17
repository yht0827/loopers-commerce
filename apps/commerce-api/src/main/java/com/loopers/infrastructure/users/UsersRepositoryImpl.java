package com.loopers.infrastructure.users;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.users.UsersModel;
import com.loopers.domain.users.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UsersRepositoryImpl implements UsersRepository {
	private final UsersJpaRepository usersJpaRepository;

	@Override
	public UsersModel save(final UsersModel usersModel) {
		return usersJpaRepository.save(usersModel);
	}

	@Override
	public Optional<UsersModel> find(final Long id) {
		return usersJpaRepository.findById(id);
	}
}
