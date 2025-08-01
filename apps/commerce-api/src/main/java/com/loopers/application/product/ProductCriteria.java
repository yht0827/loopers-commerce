package com.loopers.application.product;

import org.springframework.data.domain.Pageable;

public record ProductCriteria(String sort, Pageable pageable) {
}
