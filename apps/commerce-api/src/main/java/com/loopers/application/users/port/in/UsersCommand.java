package com.loopers.application.users.port.in;

import com.loopers.domain.users.User;

public record UsersCommand(
        String userId,
        String name,
        String email,
        String birthday,
        String gender
) {

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .name(name)
                .email(email)
                .birthday(birthday)
                .gender(gender)
                .build();
    }
}
