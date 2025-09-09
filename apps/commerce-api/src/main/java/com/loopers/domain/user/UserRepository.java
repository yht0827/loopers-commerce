package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
	User save(final User user);

	Optional<User> findByUserId(final UserId userId);

	boolean existsByUserId(final UserId userId);

	boolean existsByEmail(final Email email);
}
