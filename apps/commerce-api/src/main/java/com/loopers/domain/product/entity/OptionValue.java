package com.loopers.domain.product.entity;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.product.entity.vo.OptionSpec;

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
@Table(name = "option_values")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionValue extends BaseEntity {

	@Column(name = "option_group_id")
	private Long optionGroupId;

	@Embedded
	private OptionSpec value;

	@Builder
	public OptionValue(Long optionGroupId, OptionSpec value) {
		this.optionGroupId = optionGroupId;
		this.value = value;
	}
}
