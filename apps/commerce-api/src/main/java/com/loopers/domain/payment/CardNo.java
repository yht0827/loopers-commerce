package com.loopers.domain.payment;

import java.io.Serializable;
import java.util.regex.Pattern;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardNo implements Serializable {

	private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{4}-\\d{4}-\\d{4}-\\d{4}$");

	@Column(name = "card_no")
	private String cardNo;

	public CardNo(String cardNo) {
		if (cardNo == null || !CARD_NUMBER_PATTERN.matcher(cardNo).matches()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "카드 번호는 xxxx-xxxx-xxxx-xxxx 형식이어야 합니다.");
		}
		this.cardNo = cardNo;
	}
}
