package com.loopers.support.error;

import lombok.Getter;

@Getter
public enum ErrorMessage {
	USER_ID_REQUIRED("USER_001", "사용자 ID는 필수입니다."),
	USER_NAME_REQUIRED("USER_002", "사용자 이름은 필수입니다."),
	USER_EMAIL_REQUIRED("USER_003", "사용자 이메일은 필수입니다."),
	USER_ID_ALREADY_EXISTS("USER_004", "이미 존재하는 ID입니다."),
	USER_NOT_FOUND("USER_005", "해당 [userId = %s]의 회원을 찾을 수 없습니다."),
	USER_ID_INVALID_FORMAT("USER_006", "아이디는 영문, 숫자를 포함하여 %d자 이내여야 합니다."),
	BIRTHDAY_INVALID_FORMAT("USER_007", "생년월일은 yyyy-MM-dd 형식이어야 합니다."),
	USER_NAME_INVALID_FORMAT("USER_008", "사용자 이름은 한글, 영문만 가능하며 %d자 이내여야 합니다."),
	EMAIL_INVALID_FORMAT("USER_009", "유효하지 않은 이메일 형식입니다."),
	GENDER_INVALID("USER_010", "유효하지 않은 성별입니다.");

	private final String code;
	private final String message;

	ErrorMessage(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String format(Object... args) {
		return String.format(message, args);
	}
}
