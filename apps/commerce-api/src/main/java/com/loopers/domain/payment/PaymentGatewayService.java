package com.loopers.domain.payment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.loopers.infrastructure.payment.PgClientDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentGatewayService {
	private final PgClient pgClient;
	private final PaymentRepository paymentRepository;

	public PaymentInfo.transaction requestPaymentGateWay(final PaymentData.PaymentRequest data) {
		PgClientDto.PgPaymentRequest pgRequest = PgClientDto.PgPaymentRequest.from(data);
		return pgClient.request(pgRequest);
	}

	public void verifyPendingPayments() {
		List<Payment> pendingPayments = paymentRepository.findByStatus(TransactionStatus.PENDING);

		for (Payment payment : pendingPayments) {
			try {
				PaymentInfo.order pgOrder = pgClient.findOrder(payment.getTransactionKey().transactionKey());

				// PG 상태에 따라 동기화
				if (pgOrder == null || pgOrder.transactions() == null || pgOrder.transactions().isEmpty()) {
					log.warn("PG 응답이 비어있음. transactionKey: {}", payment.getTransactionKey().transactionKey());
					continue;
				}

				TransactionStatus pgStatus = pgOrder.transactions().getFirst().status();

				switch (pgStatus) {
					case SUCCESS -> {
						if (payment.getStatus() == TransactionStatus.PENDING) {
							payment.processPaymentSuccess();
							log.info("결제 상태 동기화: PENDING → SUCCESS, transactionKey: {}",
								payment.getTransactionKey().transactionKey());
						}
					}
					case FAILED -> {
						if (payment.getStatus() == TransactionStatus.PENDING) {
							payment.processPaymentFailed();
							log.info("결제 상태 동기화: PENDING → FAILED, transactionKey: {}",
								payment.getTransactionKey().transactionKey());
						}
					}
					case PENDING -> log.debug("PG 상태도 PENDING, 유지. transactionKey: {}",
						payment.getTransactionKey().transactionKey());
				}

			} catch (Exception e) {
				log.error("결제 상태 확인 실패. transactionKey: {}, error: {}",
					payment.getTransactionKey().transactionKey(), e.getMessage());
			}
		}

		log.info("결제 상태 확인 완료. 대상 건수: {}", pendingPayments.size());
	}
}
