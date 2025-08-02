package com.loopers.infrastructure.users;

import com.loopers.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersJpaRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
}
