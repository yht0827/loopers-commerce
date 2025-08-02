package com.loopers.infrastructure.users;

import com.loopers.domain.users.User;
import com.loopers.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UsersRepositoryImpl implements UsersRepository {
    private final UsersJpaRepository usersJpaRepository;

    @Override
    public User save(final User user) {
        return usersJpaRepository.save(user);
    }

    @Override
    public Optional<User> find(final Long id) {
        return usersJpaRepository.findById(id);
    }

    @Override
    public boolean existsByUserId(final String userId) {
        return usersJpaRepository.existsByUserId(userId);
    }
}
