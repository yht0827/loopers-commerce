package com.loopers.domain.common;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import java.io.Serializable;

import com.loopers.support.error.CoreException;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(String userId) implements Serializable {
	public UserId {
		if (userId == null || userId.isBlank()) {
			throw new CoreException(BAD_REQUEST, USER_ID_REQUIRED.getMessage());
		}
	}
}
