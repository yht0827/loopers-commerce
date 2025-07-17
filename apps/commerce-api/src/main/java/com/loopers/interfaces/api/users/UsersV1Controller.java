package com.loopers.interfaces.api.users;

import com.loopers.application.users.facade.UsersFacade;
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
        UsersResponse response = UsersResponse.from(usersFacade.join(request.toCommand()));
        return ApiResponse.success(response);
    }

    @GetMapping("/me")
    @Override
    public ApiResponse<UsersResponse> me(@RequestHeader(value = "X-USER-ID") final Long id) {
        UsersResponse response = UsersResponse.from(usersFacade.me(id));
        return ApiResponse.success(response);
    }

}
