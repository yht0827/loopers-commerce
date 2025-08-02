package com.loopers.application.users.facade;

import com.loopers.application.users.port.in.UsersCommand;
import com.loopers.application.users.port.out.UsersInfo;
import com.loopers.domain.users.User;
import com.loopers.domain.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UsersFacade {
    private final UsersService usersService;

    public UsersInfo join(final UsersCommand usersCommand) {
        User user = usersService.join(usersCommand);
        return UsersInfo.from(user);
    }

    public UsersInfo me(final Long id) {
        User user = usersService.me(id);
        return UsersInfo.from(user);
    }

}
