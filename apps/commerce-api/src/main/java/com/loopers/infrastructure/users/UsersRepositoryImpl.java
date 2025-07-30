package com.loopers.infrastructure.users;

import com.loopers.domain.users.Users;
import com.loopers.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UsersRepositoryImpl implements UsersRepository {
    private final UsersJpaRepository usersJpaRepository;

    @Override
    public Users save(final Users users) {
        return usersJpaRepository.save(users);
    }

    @Override
    public Optional<Users> find(final Long id) {
        return usersJpaRepository.findById(id);
    }

    @Override
    public boolean existsByUserId(final String userId) {
        return usersJpaRepository.existsByUserId(userId);
    }
}
