package com.loopers.interfaces.api.users.port.out;

import com.loopers.application.users.port.out.UsersInfo;

public record UsersResponse(Long id, String userId, String name, String password, String email, String phone, String birthday,
							Integer age, String gender, String description) {
	public static UsersResponse from(final UsersInfo info) {
		return new UsersResponse(
			info.id(),
			info.userId(),
			info.name(),
			info.password(),
			info.phone(),
			info.email(),
			info.birthday(),
			info.age(),
			info.gender(),
			info.description()
		);
	}
}
