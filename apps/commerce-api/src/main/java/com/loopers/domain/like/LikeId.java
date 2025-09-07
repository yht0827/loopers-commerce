package com.loopers.domain.like;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeId implements Serializable {

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "target_id")
	private Long targetId;

	public LikeId(Long userId, Long targetId) {
		if (userId == null || userId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "사용자 ID는 비어있을 수 없습니다.");
		}
		if (targetId == null || targetId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "좋아요 대상 ID는 비어있을 수 없습니다.");
		}
		this.userId = userId;
		this.targetId = targetId;
	}
}
