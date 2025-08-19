package com.loopers.domain.payment;

import java.util.Optional;

public interface PaymentHistoryRepository {

	PaymentHistory save(final PaymentHistory paymentHistory);

	Optional<PaymentHistory> findById(final Long id);
}
