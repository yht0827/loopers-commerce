package com.loopers.infrastructure.users;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.users.User;
import com.loopers.domain.users.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UsersRepositoryImpl implements UsersRepository {
	private final UsersJpaRepository usersJpaRepository;

	@Override
	public User save(final User user) {
		return usersJpaRepository.save(user);
	}

	@Override
	public Optional<User> findByUserId(final String userId) {
		return usersJpaRepository.findByUserId(userId);
	}

	@Override
	public boolean existsByUserId(final String userId) {
		return usersJpaRepository.existsByUserId(userId);
	}
}
