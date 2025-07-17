package com.loopers.infrastructure.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.users.UsersModel;

public interface UsersJpaRepository extends JpaRepository<UsersModel, Long> {
}
