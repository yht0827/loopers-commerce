package com.loopers.infrastructure.users;

import com.loopers.domain.users.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersJpaRepository extends JpaRepository<UsersEntity, Long> {
    boolean existsByUserId(String userId);
}
