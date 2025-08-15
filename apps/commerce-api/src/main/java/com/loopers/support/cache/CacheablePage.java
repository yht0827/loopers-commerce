package com.loopers.support.cache;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Redis 캐싱이 가능한 Page 래퍼 클래스
 * Spring Data의 PageImpl은 JSON 직렬화에 필요한 기본 생성자가 없어서 Redis 캐싱 시 문제가 발생
 * 이 클래스는 JSON 직렬화/역직렬화가 가능하도록 Jackson 어노테이션을 추가한 래퍼
 *
 * @param content Getters for Jackson serialization
 */
public record CacheablePage<T>(List<T> content, long totalElements, int totalPages, int pageNumber, int pageSize, boolean first,
							   boolean last) {

	@JsonCreator
	public CacheablePage(
		@JsonProperty("content") List<T> content,
		@JsonProperty("totalElements") long totalElements,
		@JsonProperty("totalPages") int totalPages,
		@JsonProperty("pageNumber") int pageNumber,
		@JsonProperty("pageSize") int pageSize,
		@JsonProperty("first") boolean first,
		@JsonProperty("last") boolean last) {
		this.content = content;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.first = first;
		this.last = last;
	}

	/**
	 * Spring Data Page를 CacheablePage로 변환
	 */
	public static <T> CacheablePage<T> from(Page<T> page) {
		return new CacheablePage<>(
			page.getContent(),
			page.getTotalElements(),
			page.getTotalPages(),
			page.getNumber(),
			page.getSize(),
			page.isFirst(),
			page.isLast()
		);
	}

	/**
	 * CacheablePage를 Spring Data Page로 변환
	 */
	public Page<T> toPage(Pageable pageable) {
		return new PageImpl<>(content, pageable, totalElements);
	}

}
