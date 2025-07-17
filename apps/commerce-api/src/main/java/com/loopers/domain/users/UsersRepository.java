package com.loopers.domain.users;

import java.util.Optional;

public interface UsersRepository {
	UsersModel save(final UsersModel usersModel);

	Optional<UsersModel> find(final Long id);
}
