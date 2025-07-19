package com.loopers.domain.users;

import java.util.Optional;

public interface UsersRepository {
	UsersEntity save(final UsersEntity usersEntity);

	Optional<UsersEntity> find(final Long id);

    boolean existsByUserId(final String userId);
}
