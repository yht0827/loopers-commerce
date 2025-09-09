package com.loopers.domain.common;

import java.io.Serializable;

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
public class Price implements Serializable {
	
	@Column(name = "price")
	private Long price;
	
	public Price(Long price) {
		if (price == null || price < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "가격은 0 이상이어야 합니다.");
		}
		this.price = price;
	}
}
