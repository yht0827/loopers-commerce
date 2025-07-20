package com.loopers.interfaces.api.users;

import com.loopers.application.users.UsersFacade;
import com.loopers.application.users.port.out.UsersInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.users.port.in.UsersRequest;
import com.loopers.interfaces.api.users.port.out.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UsersV1Controller implements UsersV1ApiSpec {

    private final UsersFacade usersFacade;

    @PostMapping
    @Override
    public ApiResponse<UsersResponse> join(@RequestBody final UsersRequest request) {
        UsersInfo usersInfo = usersFacade.join(request.toCommand());
        UsersResponse response = UsersResponse.from(usersInfo);
        return ApiResponse.success(response);
    }

    @GetMapping("/me")
    @Override
    public ApiResponse<UsersResponse> getUserById(@RequestHeader("X-USER-ID") final Long id) {
        UsersInfo usersInfo = usersFacade.getUserById(id);
        UsersResponse response = UsersResponse.from(usersInfo);
        return ApiResponse.success(response);
    }

}
