package com.loopers.domain.users;

import java.util.Optional;

public interface UsersRepository {
	User save(final User user);

	Optional<User> find(final Long id);

    boolean existsByUserId(final String userId);
}
