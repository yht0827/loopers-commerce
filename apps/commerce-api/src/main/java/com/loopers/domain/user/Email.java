package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import java.io.Serializable;
import java.util.regex.Pattern;

import com.loopers.support.error.CoreException;

import jakarta.persistence.Embeddable;

@Embeddable
public record Email(String email) implements Serializable {

	private static final Pattern EMAIL_PATTERN = Pattern.compile(
		"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

	public Email {
		if (email == null || email.isBlank()) {
			throw new CoreException(BAD_REQUEST, USER_EMAIL_REQUIRED.getMessage());
		}
		if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
			throw new CoreException(BAD_REQUEST, EMAIL_INVALID_FORMAT.getMessage());
		}
	}
}
