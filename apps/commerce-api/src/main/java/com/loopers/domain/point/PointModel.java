package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointModel extends BaseEntity {

    private Long userId;
    private Long balance = 0L;

    @Builder
    public PointModel(Long userId, Long balance) {
        this.userId = userId;
        this.balance = balance;
    }

    /**
     * 포인트를 충전합니다.
     *
     * @param amount 충전할 포인트 금액
     */
    public void charge(Long amount) {
        this.balance += amount;
    }
}
