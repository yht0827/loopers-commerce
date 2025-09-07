package com.loopers.domain.product;

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
public class ProductName implements Serializable {
	
	@Column(name = "name")
	private String name;
	
	public ProductName(String name) {
		if (name == null || name.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "상품 이름은 비어있을 수 없습니다.");
		}
		this.name = name;
	}
}
