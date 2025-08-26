package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserCommand;
import com.loopers.application.user.UserInfo;

public record UserDto() {

	public record V1() {
		public record UserRequest(
			String userId,
			String name,
			String email,
			String birthday,
			String gender
		) {
			public UserCommand.CreateUser toCommand() {
				return new UserCommand.CreateUser(userId, name, email, birthday, gender);
			}
		}

		public record UserResponse(Long id, String userId, String name, String email, String birthday, String gender) {
			public static UserResponse from(final UserInfo info) {
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
