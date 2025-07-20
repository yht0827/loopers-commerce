package com.loopers.interfaces.api.users.port.in;

import com.loopers.application.users.port.in.UsersCommand;
import jakarta.validation.constraints.NotNull;

public record UsersRequest(
        @NotNull String userId,
        @NotNull String name,
        @NotNull String email,
        @NotNull String birthday,
        @NotNull String gender
) {
    public UsersCommand toCommand() {
        return new UsersCommand(userId, name, email, birthday, gender);
    }
}
