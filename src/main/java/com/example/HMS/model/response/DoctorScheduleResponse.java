package com.example.HMS.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorScheduleResponse {

	private String doctorName;
	private String date;
	private String startTime;
	private String endTime;
	private String specialization;
	private String departmentName;
	private boolean isAvailable;
}
