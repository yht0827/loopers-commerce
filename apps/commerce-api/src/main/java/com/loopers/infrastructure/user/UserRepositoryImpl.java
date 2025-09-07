package com.loopers.infrastructure.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;

	@Override
	public User save(final User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public Optional<User> findByUserId(final String userId) {
		return userJpaRepository.findByUserId(userId);
	}

	@Override
	public boolean existsByUserId(final String userId) {
		return userJpaRepository.existsByUserIdUserId(userId);
	}
}
