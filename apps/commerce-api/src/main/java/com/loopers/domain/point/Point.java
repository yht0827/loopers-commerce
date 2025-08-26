package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.common.UserId;

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
	private Balance balance;

	@Builder
	public Point(final UserId userId, final Balance balance) {
		this.userId = userId;
		this.balance = balance;
	}

	public void charge(final Balance balance) {
		this.balance = this.balance.charge(balance);
	}
}
