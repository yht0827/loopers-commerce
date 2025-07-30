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
    public Users join(final UsersCommand usersCommand) {
        Users entity = usersCommand.toEntity();
        validationDuplicateId(entity);

        return usersRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Users me(final Long id) {
        return usersRepository.find(id)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 [id = " + id + "]의 회원을 찾을 수 없습니다."));
    }

    private void validationDuplicateId(final Users entity) {
        if (usersRepository.existsByUserId(entity.getUserId())) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이미 존재하는 ID 입니다.");
        }
    }

}
