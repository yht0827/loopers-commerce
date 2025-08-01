package com.loopers.domain.like;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

	@EmbeddedId
	private LikeId targetId;

	@CreatedDate
	private ZonedDateTime createdAt;

	@Builder
	public Like(LikeId targetId) {
		this.targetId = targetId;
	}

}
