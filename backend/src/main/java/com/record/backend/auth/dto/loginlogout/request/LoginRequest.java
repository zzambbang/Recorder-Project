package com.record.backend.auth.dto.loginlogout.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {

	@NotBlank
	private String email;

	@NotBlank
	private String password;
}
