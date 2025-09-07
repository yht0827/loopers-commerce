package com.loopers.domain.payment;

import java.util.regex.Pattern;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CallbackUrl {

	private static final Pattern CALLBACK_URL_PATTERN = Pattern.compile("^https://.+$");

	private String callbackUrl;

	public CallbackUrl(String callbackUrl) {
		if (callbackUrl == null || !CALLBACK_URL_PATTERN.matcher(callbackUrl).matches()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "콜백 URL은 https://로 시작해야 합니다.");
		}
		this.callbackUrl = callbackUrl;
	}
}
