package com.loopers.infrastructure.users;

import com.loopers.domain.users.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersJpaRepository extends JpaRepository<UsersModel, Long> {
    boolean existsByUserId(String userId);
}
