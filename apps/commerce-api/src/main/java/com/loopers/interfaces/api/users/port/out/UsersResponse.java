package com.loopers.interfaces.api.users.port.out;

import com.loopers.application.users.port.out.UsersInfo;

public record UsersResponse(Long id, String userId, String name, String email, String birthday, String gender) {
    public static UsersResponse from(final UsersInfo info) {
        return new UsersResponse(
                info.id(),
                info.userId(),
                info.name(),
                info.email(),
                info.birthday(),
                info.gender()
        );
    }
}
