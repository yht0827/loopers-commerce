package com.loopers.application.users.port.out;

import com.loopers.domain.users.UsersModel;

public record UsersInfo(Long id, String name, String password, String email, String phone, Integer age, String gender,
						String description) {
	public static UsersInfo from(final UsersModel model) {
		return new UsersInfo(
			model.getId(),
			model.getName(),
			model.getPassword(),
			model.getEmail(),
			model.getPhone(),
			model.getAge(),
			model.getGender(),
			model.getDescription()
		);
	}
}
