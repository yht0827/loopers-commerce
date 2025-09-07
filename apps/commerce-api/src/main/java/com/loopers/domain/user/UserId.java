package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import java.io.Serializable;

import com.loopers.support.error.CoreException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId implements Serializable {

	@Column(name = "user_id")
	private String userId;

	public UserId(String userId) {
		if (userId == null || userId.isBlank()) {
			throw new CoreException(BAD_REQUEST, USER_ID_REQUIRED.getMessage());
		}
		this.userId = userId;
	}
}
