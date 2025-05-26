package com.example.HMS.dto;

import com.example.HMS.model.entities.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
	
	private long id;
	private String name;
	private String email;
	private String phone;
	private String address;
	private Role role;

}