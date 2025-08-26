package com.loopers.application.user;

import com.loopers.domain.user.User;

public record UserInfo(Long id, String userId, String name, String email, String birthday, String gender) {
	public static UserInfo from(final User user) {
		return new UserInfo(
			user.getId(),
			user.getUserId().userId(),
			user.getName().name(),
			user.getEmail().email(),
			user.getBirthday().birthday(),
			user.getGender().name()
		);
	}
}
