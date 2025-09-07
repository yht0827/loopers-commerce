package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Embedded;
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
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Embedded
	private UserId userId;

	@Embedded
	private UserName name;

	@Embedded
	private Email email;

	@Embedded
	private Birthday birthday;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Builder
	public User(UserId userId, UserName name, Email email, Birthday birthday, Gender gender) {
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.birthday = birthday;
		this.gender = gender;
	}
}
