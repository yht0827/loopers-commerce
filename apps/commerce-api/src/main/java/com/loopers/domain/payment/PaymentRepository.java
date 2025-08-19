package com.loopers.domain.payment;

import java.util.Optional;

public interface PaymentRepository {

	Payment save(final Payment payment);

	Optional<Payment> findById(final Long id);
}
