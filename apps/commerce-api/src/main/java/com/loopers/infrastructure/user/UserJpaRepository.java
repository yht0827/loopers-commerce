package com.loopers.infrastructure.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.user.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

	boolean existsByUserId_UserId(String userId);

	@Query("SELECT u FROM User u WHERE u.userId.userId = :userId")
	Optional<User> findByUserId(String userId);
}
