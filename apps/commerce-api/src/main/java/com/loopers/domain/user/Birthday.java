package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import java.io.Serializable;
import java.util.regex.Pattern;

import com.loopers.support.error.CoreException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Birthday implements Serializable {

	private static final Pattern BIRTHDAY_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

	@Column(name = "birthday")
	private String birthday;

	public Birthday(String birthday) {
		if (birthday == null || !BIRTHDAY_PATTERN.matcher(birthday).matches()) {
			throw new CoreException(BAD_REQUEST, BIRTHDAY_INVALID_FORMAT.getMessage());
		}
		this.birthday = birthday;
	}
}
