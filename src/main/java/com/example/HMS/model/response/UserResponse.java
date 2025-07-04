package com.example.HMS.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

	private Long id;
	private boolean isActive;
	private String name;
	private String email;
	private String phone;
	private String address;
	private String role;
}