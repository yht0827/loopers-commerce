package com.loopers.application.user;

public interface UserUseCase {

    UserResult createUser(final CreateUserCommand userCommand);

    UserResult getUser(final GetUserQuery query);
}
