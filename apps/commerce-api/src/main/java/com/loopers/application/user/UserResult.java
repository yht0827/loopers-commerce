package com.loopers.application.user;

import com.loopers.domain.user.User;

public record UserResult(Long id, String userId, String name, String email, String birthday, String gender) {
	public static UserResult from(final User user) {
		return new UserResult(
			user.getId(),
			user.getUserId().getUserId(),
			user.getName().getName(),
			user.getEmail().getEmail(),
			user.getBirthday().getBirthday(),
			user.getGender().name()
		);
	}
}

