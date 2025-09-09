package com.loopers.application.point;

public record GetPointQuery(String userId) {
	public static GetPointQuery of(final String userId) {
		return new GetPointQuery(userId);
	}
}
