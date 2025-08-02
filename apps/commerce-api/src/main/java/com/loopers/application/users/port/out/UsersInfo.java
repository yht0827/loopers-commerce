package com.loopers.application.users.port.out;

import com.loopers.domain.users.User;

public record UsersInfo(Long id, String userId, String name, String email, String birthday, String gender) {
    public static UsersInfo from(final User model) {
        return new UsersInfo(
                model.getId(),
                model.getUserId(),
                model.getName(),
                model.getEmail(),
                model.getBirthday(),
                model.getGender()
        );
    }
}
