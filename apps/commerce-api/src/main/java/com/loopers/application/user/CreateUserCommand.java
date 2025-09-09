package com.loopers.application.user;

public record CreateUserCommand(
	String userId,
	String email,
	String birthday,
	String gender
) {
}

