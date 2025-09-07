package com.loopers.domain.payment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.loopers.application.payment.PaymentCommand;
import com.loopers.infrastructure.payment.PgClientDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentGatewayService {
	private final PgClient pgClient;
	private final PaymentRepository paymentRepository;
	private final PaymentStatusSynchronizer paymentStatusSynchronizer;

	public PaymentInfo.transaction requestPaymentGateWay(final PaymentData.PaymentRequest data) {
		PgClientDto.PgPaymentRequest pgRequest = PgClientDto.PgPaymentRequest.from(data);
		return pgClient.request(pgRequest);
	}

	public void verifyPendingPayments() {
		List<Payment> pendingPayments = paymentRepository.findByStatus(TransactionStatus.PENDING);
		log.info("결제 상태 확인 시작. 대상 건수: {}", pendingPayments.size());

		for (Payment payment : pendingPayments) {
			try {
				verifyAndSyncPaymentStatusByScheduler(payment);
			} catch (Exception e) {
				log.error("결제 상태 확인 실패. transactionKey: {}, error: {}",
					payment.getTransactionKey().getTransactionKey(), e.getMessage());
			}
		}

		log.info("결제 상태 확인 완료. 대상 건수: {}", pendingPayments.size());
	}

	private void verifyAndSyncPaymentStatusByScheduler(Payment payment) {
		PaymentInfo.order pgOrder = pgClient.findOrder(payment.getOrderId().getOrderId());

		// PG 상태에 따라 동기화
		if (pgOrder == null || pgOrder.transactions() == null || pgOrder.transactions().isEmpty()) {
			log.warn("PG 응답이 비어있음. transactionKey: {}", payment.getTransactionKey().getTransactionKey());
			return;
		}

		TransactionStatus pgStatus = pgOrder.transactions().getFirst().status();
		paymentStatusSynchronizer.processScheduler(payment, pgStatus);
	}

	public PaymentInfo processCallbackWithVerification(Payment payment, PaymentCommand.ProcessCallback command) {
		try {
			PaymentInfo.transaction pgTransaction = pgClient.findTransaction(command.transactionKey());
			paymentStatusSynchronizer.processCallback(payment, command, pgTransaction);
		} catch (Exception e) {
			log.error("PG 조회 실패, 콜백 데이터로 처리. transactionKey: {}, error: {}",
				command.transactionKey(), e.getMessage());
			paymentStatusSynchronizer.processCallbackWithException(payment, command);
		}

		return PaymentInfo.from(payment);
	}
}
