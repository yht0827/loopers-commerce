package com.loopers.application.users.port.in;

import com.loopers.domain.users.UsersEntity;

public record UsersCommand(
        String userId,
        String name,
        String email,
        String birthday,
        String gender
) {

    public UsersEntity toEntity() {
        return UsersEntity.builder()
                .userId(userId)
                .name(name)
                .email(email)
                .birthday(birthday)
                .gender(gender)
                .build();
    }
}
