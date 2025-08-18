package com.loopers.domain.users;

import java.util.Optional;

public interface UsersRepository {
	User save(final User user);

	Optional<User> findByUserId(final String userId);

    boolean existsByUserId(final String userId);
}
