package com.loopers.application.users.port.out;

import com.loopers.domain.users.UsersModel;

public record UsersInfo(Long id, String userId, String name, String password, String email, String phone, String birthday,
						Integer age, String gender, String description) {
	public static UsersInfo from(final UsersModel model) {
		return new UsersInfo(
			model.getId(),
			model.getUserId(),
			model.getName(),
			model.getPassword(),
			model.getEmail(),
			model.getPhone(),
			model.getBirthday(),
			model.getAge(),
			model.getGender(),
			model.getDescription()
		);
	}
}
