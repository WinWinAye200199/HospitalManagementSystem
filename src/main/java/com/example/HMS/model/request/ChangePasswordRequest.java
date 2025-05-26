package com.example.HMS.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotBlank
public class ChangePasswordRequest {

	private String oldPassword;
	private String newPassword;
	
}
