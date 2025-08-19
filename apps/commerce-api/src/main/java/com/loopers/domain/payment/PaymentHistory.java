package com.loopers.domain.payment;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.common.PaymentId;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payment_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory extends BaseEntity {

	private PaymentId paymentId;

	@Enumerated(EnumType.STRING)
	private TransactionStatus oldStatus;

	@Enumerated(EnumType.STRING)
	private TransactionStatus newStatus;

	private PaymentReason reason;

	@Builder
	public PaymentHistory(PaymentId paymentId, TransactionStatus oldStatus, TransactionStatus newStatus,
		PaymentReason reason) {
		this.paymentId = paymentId;
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
		this.reason = reason;
	}
}
