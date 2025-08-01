package com.loopers.domain.product;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public record LikeCount(Long likeCount) implements Serializable {
}
