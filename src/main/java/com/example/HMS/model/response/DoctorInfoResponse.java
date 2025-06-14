package com.example.HMS.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorInfoResponse {

	private long id;
	private String name;
	private String email;
	private String phone;
	private String address;
	private String specialization;
	private String departmentName;
}
