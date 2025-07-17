package com.loopers.interfaces.api.users.port.in;

import com.loopers.application.users.port.in.UsersCommand;

public record UsersRequest(
        String userId,
        String name,
        String email,
        String birthday,
        String gender
) {
    public UsersCommand toCommand() {
        return new UsersCommand(userId, name, email, birthday, gender);
    }
}
