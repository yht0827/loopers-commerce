package com.loopers.infrastructure.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.users.User;

public interface UsersJpaRepository extends JpaRepository<User, Long> {
	boolean existsByUserId(String userId);

	Optional<User> findByUserId(String userId);
}
