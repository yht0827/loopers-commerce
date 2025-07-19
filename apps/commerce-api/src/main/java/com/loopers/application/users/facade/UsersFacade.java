package com.loopers.application.users.facade;

import com.loopers.application.users.port.in.UsersCommand;
import com.loopers.application.users.port.out.UsersInfo;
import com.loopers.domain.users.UsersEntity;
import com.loopers.domain.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UsersFacade {
    private final UsersService usersService;

    public UsersInfo join(final UsersCommand usersCommand) {
        UsersEntity usersEntity = usersService.join(usersCommand);
        return UsersInfo.from(usersEntity);
    }

    public UsersInfo me(final Long id) {
        UsersEntity usersEntity = usersService.me(id);
        return UsersInfo.from(usersEntity);
    }

}
