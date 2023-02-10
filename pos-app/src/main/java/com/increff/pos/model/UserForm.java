package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserForm {

	@NotBlank
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String role;

	@Override
	public String toString() {
		return "UserForm{" +
				"email='" + email + '\'' +
				", password='" + password + '\'' +
				", role='" + role + '\'' +
				'}';
	}
}
