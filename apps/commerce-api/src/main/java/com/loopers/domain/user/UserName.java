package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import java.io.Serializable;
import java.util.regex.Pattern;

import com.loopers.support.error.CoreException;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserName(String name) implements Serializable {

	private static final Pattern VALID_USER_NAME_PATTERN = Pattern.compile("^[a-zA-Z가-힣\\s]{1,10}$");
	private static final int MAX_LENGTH = 10;

	public UserName {
		if (name == null || name.isBlank()) {
			throw new CoreException(BAD_REQUEST, USER_NAME_REQUIRED.getMessage());
		}
		if (!VALID_USER_NAME_PATTERN.matcher(name.trim()).matches()) {
			throw new CoreException(BAD_REQUEST, USER_NAME_INVALID_FORMAT.format(MAX_LENGTH));
		}
	}
}
