package com.example.HMS.service;

import java.util.List;

import com.example.HMS.model.request.DoctorScheduleRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.DoctorScheduleResponse;

public interface DoctorScheduleService {

	ApiResponse addSchedule(DoctorScheduleRequest request);
	List<DoctorScheduleResponse> getAllSchedules();
	List<DoctorScheduleResponse> getDoctorWithSpecialization(String specialization);
}
