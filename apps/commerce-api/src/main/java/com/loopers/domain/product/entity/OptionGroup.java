package com.loopers.domain.product.entity;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.product.entity.vo.GroupName;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "option_groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionGroup extends BaseEntity {

	@Column(name = "product_id")
	private Long productId;

	@Embedded
	private GroupName name;

	@Builder
	public OptionGroup(Long productId, GroupName name) {
		this.productId = productId;
		this.name = name;
	}
}
