package com.loopers.domain.point;

import com.loopers.domain.BaseTimeEntity;
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
@Table(name = "points")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseTimeEntity {

	private Long userId;
	private Long balance = 0L;

	@Builder
	public Point(Long userId, Long balance) {
		validateBalance(balance);

		this.userId = userId;
		this.balance = balance;
	}

	private void validateBalance(Long balance) {
		if (balance == null || balance < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "잔액은 0보다 작을 수 없습니다.");
		}
	}

	/**
	 * 포인트를 충전합니다.
	 *
	 * @param amount 충전할 포인트 금액
	 */
	public void charge(Long amount) {
		this.balance += amount;
	}

	public void use() {
		if (this.balance <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "포인트가 부족합니다.");
		}
		this.balance--;
	}

}
