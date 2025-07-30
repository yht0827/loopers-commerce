package com.loopers.infrastructure.users;

import com.loopers.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersJpaRepository extends JpaRepository<Users, Long> {
    boolean existsByUserId(String userId);
}
