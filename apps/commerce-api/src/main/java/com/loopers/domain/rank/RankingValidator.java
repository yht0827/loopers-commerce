package com.loopers.domain.rank;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RankingValidator {

	public LocalDate validateDate(final LocalDate date) {
		if (date == null) {
			return LocalDate.now();
		}

		if (date.isAfter(LocalDate.now())) {
			throw new CoreException(ErrorType.BAD_REQUEST, "랭킹 조회 날짜는 미래일 수 없습니다.");
		}

		return date;
	}
}
