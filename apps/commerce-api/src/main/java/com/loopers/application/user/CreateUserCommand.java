package com.loopers.application.user;

import com.loopers.domain.user.UserId;
import com.loopers.domain.user.Birthday;
import com.loopers.domain.user.Email;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserName;

public record CreateUserCommand(
    String userId,
    String name,
    String email,
    String birthday,
    String gender
) {
    public User toEntity() {
        return User.builder()
            .userId(new UserId(userId))
            .name(new UserName(name))
            .email(new Email(email))
            .birthday(new Birthday(birthday))
            .gender(Gender.of(gender))
            .build();
    }
}

