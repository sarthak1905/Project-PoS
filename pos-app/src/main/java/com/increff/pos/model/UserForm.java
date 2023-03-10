package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserForm {

	@NotBlank
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String role;

}
