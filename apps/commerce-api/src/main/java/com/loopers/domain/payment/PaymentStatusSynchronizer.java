package com.loopers.domain.payment;

import org.springframework.stereotype.Component;

import com.loopers.application.payment.PaymentCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentStatusSynchronizer {

	public void processCallback(Payment payment, PaymentCommand.ProcessCallback command,
		PaymentInfo.transaction pgTransaction) {
		if (isCallbackDataValid(command, pgTransaction)) {
			updatePaymentStatus(payment, command.status());
			log.info("콜백 교차검증 성공. transactionKey: {}, status: {}",
				command.transactionKey(), command.status());
		} else {
			updatePaymentStatus(payment, pgTransaction.status());
			log.warn("콜백 교차검증 실패, PG 실제 상태로 업데이트. transactionKey: {}, callback: {}, pg: {}",
				command.transactionKey(), command.status(), pgTransaction.status());
		}
	}

	public void processCallbackWithException(Payment payment, PaymentCommand.ProcessCallback command) {
		log.error("콜백 교차검증 실패, 콜백 데이터로 처리. transactionKey: {}", command.transactionKey());
		updatePaymentStatus(payment, command.status());
	}

	public void processScheduler(Payment payment, TransactionStatus pgStatus) {
		if (pgStatus != TransactionStatus.PENDING && payment.getStatus() == TransactionStatus.PENDING) {
			updatePaymentStatus(payment, pgStatus);
			log.info("스케줄러 - 결제 상태 동기화: PENDING → {}, transactionKey: {}",
				pgStatus, payment.getTransactionKey().transactionKey());
		}
	}

	private boolean isCallbackDataValid(PaymentCommand.ProcessCallback command, PaymentInfo.transaction pgTransaction) {
		if (pgTransaction == null || pgTransaction.status() == null) {
			return false;
		}
		return command.status() == pgTransaction.status();
	}

	private void updatePaymentStatus(Payment payment, TransactionStatus status) {
		switch (status) {
			case SUCCESS -> payment.processPaymentSuccess();
			case FAILED -> payment.processPaymentFailed();
			case PENDING -> payment.processPaymentPending();
		}
	}

	public enum PaymentSyncResult {
		SUCCESS, FALLBACK_TO_PG, ERROR
	}
}
