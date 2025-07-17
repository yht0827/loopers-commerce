package com.loopers.application.users.port.in;

import com.loopers.domain.users.UsersModel;

public record UsersCommand(
        String userId,
        String name,
        String email,
        String birthday,
        String gender
) {

    public UsersModel toEntity() {
        return UsersModel.builder()
                .userId(userId)
                .name(name)
                .email(email)
                .birthday(birthday)
                .gender(gender)
                .build();
    }
}
