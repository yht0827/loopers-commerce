package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.common.UserId;
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
public class Point extends BaseEntity {

	private UserId userId;
	private Long balance = 0L;

	@Builder
	public Point(UserId userId, Long balance) {
		validateBalance(balance);

		this.userId = userId;
		this.balance = balance;
	}

	private void validateBalance(final Long balance) {
		if (balance == null || balance < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "잔액은 0보다 작을 수 없습니다.");
		}
	}

	public void charge(final Long amount) {
		if (this.balance < 1) {
			throw new CoreException(ErrorType.BAD_REQUEST, "충전할 금액은 1 이상이어야 합니다.");
		}
		this.balance += amount;
	}

	public void use(final Long amount) {
		if (this.balance < amount) {
			throw new CoreException(ErrorType.BAD_REQUEST, "보유한 포인트가 부족합니다.");
		}
		this.balance -= amount;
	}

}
