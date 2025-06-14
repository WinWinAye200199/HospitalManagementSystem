package com.example.HMS.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NurseResponse {

	private long id;
	private String nurseName;
	private String date;
	private String startTime;
	private String endTime;
	private String departmentName;
	private String assignedDoctorName;
}
