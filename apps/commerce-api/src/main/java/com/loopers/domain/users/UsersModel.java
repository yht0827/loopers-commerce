package com.loopers.domain.users;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsersModel extends BaseEntity {

	private String name;
	private String password;
	private String email;
	private String phone;
	private Integer age;
	private String gender;
	private String description;

	@Builder
	public UsersModel(String name, String password, String email, String phone, Integer age, String gender, String description) {

		if (name == null || name.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "이름은 비어있을 수 없습니다.");
		}
		if (description == null || description.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "설명은 비어있을 수 없습니다.");
		}

		this.name = name;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.age = age;
		this.gender = gender;
		this.description = description;
	}
}
