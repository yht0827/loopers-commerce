package com.loopers.application.users.port.in;

import com.loopers.domain.users.Users;

public record UsersCommand(
        String userId,
        String name,
        String email,
        String birthday,
        String gender
) {

    public Users toEntity() {
        return Users.builder()
                .userId(userId)
                .name(name)
                .email(email)
                .birthday(birthday)
                .gender(gender)
                .build();
    }
}
