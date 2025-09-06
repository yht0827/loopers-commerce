package com.loopers.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.EventHandled;

public interface EventHandledJpaRepository extends JpaRepository<EventHandled, Long> {

	Optional<EventHandled> findByEventIdAndConsumerGroup(String eventId, String consumerGroup);

}
