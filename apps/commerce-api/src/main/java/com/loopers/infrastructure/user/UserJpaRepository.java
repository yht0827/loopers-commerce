package com.loopers.infrastructure.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.user.Email;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserId;

public interface UserJpaRepository extends JpaRepository<User, Long> {

	boolean existsByUserId(UserId userId);

	boolean existsByEmail(Email email);

	@Query("SELECT u FROM User u WHERE u.userId = :userId")
	Optional<User> findByUserId(UserId userId);
}
