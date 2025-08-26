package com.loopers.application.user;

import com.loopers.domain.user.User;

public record UserInfo(Long id, String userId, String name, String email, String birthday, String gender) {
	public static UserInfo from(final User model) {
		return new UserInfo(
			model.getId(),
			model.getUserId().userId(),
			model.getName().name(),
			model.getEmail().email(),
			model.getBirthday().birthday(),
			model.getGender().name()
		);
	}
}
