package com.loopers.domain.users;

import com.loopers.application.users.port.in.UsersCommand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UsersService {
    private final UsersRepository usersRepository;

    @Transactional
    public UsersModel join(final UsersCommand usersCommand) {
        UsersModel entity = usersCommand.toEntity();
        return usersRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public UsersModel me(final Long id) {
        return usersRepository.find(id)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 [id = " + id + "]의 회원을 찾을 수 없습니다."));
    }
}
