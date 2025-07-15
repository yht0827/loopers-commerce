package com.loopers.domain.users;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class UsersModel extends BaseEntity {

	@Column(name = "user_name")
	private String userName;
	private String password;
	private String email;
	private String name;
	private String phone;

	protected UsersModel() {
	}

	public UsersModel(String userName, String password, String email, String name, String phone) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.name = name;
		this.phone = phone;
	}
}
