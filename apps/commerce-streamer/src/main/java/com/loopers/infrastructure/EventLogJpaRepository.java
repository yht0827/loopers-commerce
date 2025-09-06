package com.loopers.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.EventLog;

public interface EventLogJpaRepository extends JpaRepository<EventLog, Long> {

}
