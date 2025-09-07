package com.loopers.interfaces.api.user;

import com.loopers.application.user.CreateUserCommand;
import com.loopers.application.user.UserResult;

public record UserDto() {

	public record V1() {
		public record UserRequest(
			String userId,
			String name,
			String email,
			String birthday,
			String gender
		) {
			public CreateUserCommand toCommand() {
				return new CreateUserCommand(userId, name, email, birthday, gender);
			}
		}

		public record UserResponse(Long id, String userId, String name, String email, String birthday, String gender) {
			public static UserResponse from(final UserResult info) {
				return new UserResponse(
					info.id(),
					info.userId(),
					info.name(),
					info.email(),
					info.birthday(),
					info.gender()
				);
			}
		}
	}
}
