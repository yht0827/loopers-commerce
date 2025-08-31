package com.loopers.application.user;

public record UserQuery() {
	public record GetUser(String userId) {
		public static UserQuery.GetUser of(final String userId) {
			return new UserQuery.GetUser(userId);
		}
	}
}
