package com.loopers.domain.like;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

	@EmbeddedId
	private LikeId targetId;

	@Version
	private Long version;

	@Builder
	public Like(LikeId targetId) {
		this.targetId = targetId;
	}

}
