package com.example.HMS.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotBlank
public class UpdateStaffRequest {
	private String name;
	private String email;
	private String phone;
	private String address;
	
    private String specialization;
    private String department;
}
