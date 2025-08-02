package com.loopers.domain.users;

import java.util.regex.Pattern;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	private static final Pattern USER_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]{1,10}$");
	private static final Pattern BIRTHDAY_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
	private static final Pattern EMAIL_PATTERN = Pattern.compile(
		"^[a-zA-Z0-9_!#$%&'*+/=?`^{|}~-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`^{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)"
			+ "+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$");

	private String userId;
	private String name;
	private String email;
	private String birthday;
	private String gender;

	@Builder
	public User(String userId, String name, String email, String birthday, String gender) {
		validateUserId(userId);
		validateBirthday(birthday);
		validateEmail(email);
		validateGender(gender);

		this.userId = userId;
		this.name = name;
		this.email = email;
		this.birthday = birthday;
		this.gender = gender;
	}

	private void validateGender(String gender) {
		if (gender == null || gender.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "성별은 비어있을 수 없습니다.");
		}
	}

	private void validateUserId(String userId) {
		if (userId == null || !USER_ID_PATTERN.matcher(userId).matches()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "아이디는 영문, 숫자를 포함하여 10자 이내여야 합니다.");
		}
	}

	private void validateBirthday(String birthday) {
		if (birthday == null || !BIRTHDAY_PATTERN.matcher(birthday).matches()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 yyyy-MM-dd 형식이어야 합니다.");
		}
	}

	private void validateEmail(String email) {
		if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "유효하지 않은 이메일 형식입니다.");
		}
	}

}
