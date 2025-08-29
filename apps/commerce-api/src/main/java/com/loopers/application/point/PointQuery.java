package com.loopers.application.point;

public record PointQuery() {
	public record GetPoint(String userId) {
		public static PointQuery.GetPoint of(final String userId) {
			return new PointQuery.GetPoint(userId);
		}
	}
}
