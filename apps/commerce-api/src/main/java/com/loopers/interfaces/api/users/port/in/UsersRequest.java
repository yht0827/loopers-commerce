package com.loopers.interfaces.api.users.port.in;

import com.loopers.application.users.port.in.UsersCommand;

public record UsersRequest(
	String userId,
	String name,
	String password,
	String email,
	String phone,
	String birthday,
	Integer age,
	String gender,
	String description
) {
	public UsersCommand toCommand() {
		return new UsersCommand(userId, name, password, email, phone, birthday, age, gender, description);
	}
}
