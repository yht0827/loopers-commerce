package com.loopers.support.util;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.servlet.http.HttpServletRequest;

public class UserIdentifier {
    private static final String USER_ID_HEADER = "X-USER-ID";

    public static Long getUserId(final Long id) {

        if (id == null || id.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "X-USER-ID 헤더가 필요합니다.");
        }

        return Long.valueOf(userId);
    }
}
