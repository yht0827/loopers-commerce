package com.loopers.interfaces.api.users;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.users.facade.UsersFacade;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.users.port.in.UsersRequest;
import com.loopers.interfaces.api.users.port.out.UsersResponse;

import lombok.RequiredArgsConstructor;

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
	public ApiResponse<UsersResponse> me(@RequestHeader("X-USER-ID") final String userId) {
		UsersResponse response = UsersResponse.from(usersFacade.me(userId));
		return ApiResponse.success(response);
	}

}
