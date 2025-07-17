package com.loopers.application.users.port.in;

import com.loopers.domain.users.UsersModel;

public record UsersCommand(
	String name,
	String password,
	String email,
	String phone,
	Integer age,
	String gender,
	String description
) {

	public UsersModel toEntity() {
		return UsersModel.builder()
			.name(name)
			.password(password)
			.email(email)
			.phone(phone)
			.age(age)
			.gender(gender)
			.description(description)
			.build();
	}
}
