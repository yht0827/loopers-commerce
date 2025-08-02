package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    private TotalOrderPrice totalOrderPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Builder
    public Order(Long userId, TotalOrderPrice totalOrderPrice, OrderStatus status) {
        this.userId = userId;
        this.totalOrderPrice = totalOrderPrice;
        this.status = status;
    }
}
