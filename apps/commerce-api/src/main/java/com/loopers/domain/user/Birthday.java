package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import java.io.Serializable;
import java.util.regex.Pattern;

import com.loopers.support.error.CoreException;

import jakarta.persistence.Embeddable;

@Embeddable
public record Birthday(String birthday) implements Serializable {

	private static final Pattern BIRTHDAY_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

	public Birthday {
		if (birthday == null || !BIRTHDAY_PATTERN.matcher(birthday).matches()) {
			throw new CoreException(BAD_REQUEST, BIRTHDAY_INVALID_FORMAT.getMessage());
		}
	}
}
