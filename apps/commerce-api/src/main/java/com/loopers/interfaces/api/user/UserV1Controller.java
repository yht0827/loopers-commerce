package com.loopers.interfaces.api.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.user.UserApplicationService;
import com.loopers.application.user.UserCommand;
import com.loopers.application.user.UserInfo;
import com.loopers.application.user.UserQuery;
import com.loopers.interfaces.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {

	private final UserApplicationService userApplicationService;

	@PostMapping
	@Override
	public ApiResponse<UserDto.V1.UserResponse> createUser(@RequestBody final UserDto.V1.UserRequest request) {
		UserCommand.CreateUser command = request.toCommand();
		UserInfo userInfo = userApplicationService.createUser(command);
		UserDto.V1.UserResponse response = UserDto.V1.UserResponse.from(userInfo);
		return ApiResponse.success(response);
	}

	@GetMapping("/me")
	@Override
	public ApiResponse<UserDto.V1.UserResponse> getUser(@RequestHeader("X-USER-ID") final String userId) {
		UserQuery.GetUser query = UserQuery.GetUser.of(userId);
		UserInfo userInfo = userApplicationService.getUser(query);
		UserDto.V1.UserResponse response = UserDto.V1.UserResponse.from(userInfo);
		return ApiResponse.success(response);
	}

}
