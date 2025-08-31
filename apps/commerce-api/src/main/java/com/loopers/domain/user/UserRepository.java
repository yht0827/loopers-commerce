package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
	User save(final User user);

	Optional<User> findByUserId(final String userId);

	boolean existsByUserId(final String userId);
}
