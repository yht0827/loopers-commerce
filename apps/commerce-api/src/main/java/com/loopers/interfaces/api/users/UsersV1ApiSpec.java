package com.loopers.interfaces.api.users;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.users.port.in.UsersRequest;
import com.loopers.interfaces.api.users.port.out.UsersResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users V1 API", description = "Users API 입니다.")
public interface UsersV1ApiSpec {

	@Operation(
		summary = "회원 가입",
		description = "회원 가입을 합니다."
	)
	ApiResponse<UsersResponse> join(UsersRequest request);

	@Operation(
		summary = "내 정보 조회",
		description = "내 정보를 조회 합니다."
	)
	ApiResponse<UsersResponse> me(final String userId);
}
