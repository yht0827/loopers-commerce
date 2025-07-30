package com.loopers.domain.like.entity;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.like.entity.vo.TargetType;

import jakarta.persistence.Column;
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
@Table(name = "Likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseEntity {

	@Column(name = "target_id", nullable = false)
	private Long targetId;

	@Enumerated(EnumType.STRING)
	@Column(name = "target_type", nullable = false)
	private TargetType targetType;

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private ZonedDateTime createdAt;

	@Builder
	public Like(Long targetId, TargetType targetType) {
		this.targetId = targetId;
		this.targetType = targetType;
	}
}
