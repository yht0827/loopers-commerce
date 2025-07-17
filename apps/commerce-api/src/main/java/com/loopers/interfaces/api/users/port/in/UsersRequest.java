package com.loopers.interfaces.api.users.port.in;

import com.loopers.application.users.port.in.UsersCommand;

public record UsersRequest(
	String name,
	String password,
	String email,
	String phone,
	Integer age,
	String gender,
	String description
) {
	public UsersCommand toCommand() {
		return new UsersCommand(name, password, email, phone, age, gender, description);
	}
}
