package com.loopers.domain.users;

import java.util.Optional;

public interface UsersRepository {
	Users save(final Users users);

	Optional<Users> find(final Long id);

    boolean existsByUserId(final String userId);
}
