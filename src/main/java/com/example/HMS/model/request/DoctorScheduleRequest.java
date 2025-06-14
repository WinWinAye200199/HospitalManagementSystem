package com.example.HMS.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorScheduleRequest {

	private Long doctorId;
	private String date;
	private String startTime;
	private String endTime;
}
