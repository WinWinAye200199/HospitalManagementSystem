package com.example.HMS.model.request;

import javax.validation.constraints.NotBlank;

import com.example.HMS.model.entities.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotBlank
public class UserRequest {

	private String name;
	private String email;
	private String phone;
	private String address;
	private String password;
	private String confirmedPassword; 
	private Role role; // Either ADMIN or STAFF
    
	
}
