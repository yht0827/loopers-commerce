package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserCommand;
import com.loopers.application.user.UserInfo;
import com.loopers.application.user.UserQuery;

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

		public record GetProfile(String userId) {
			public UserQuery.GetUser toQuery(final String userId) {
				return new UserQuery.GetUser(userId);
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
