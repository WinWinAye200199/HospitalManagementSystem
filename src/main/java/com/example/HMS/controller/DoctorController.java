package com.example.HMS.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HMS.dto.UserDto;
import com.example.HMS.model.request.MedicalRecordRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.AppointmentResposne;
import com.example.HMS.model.response.DoctorInfoResponse;
import com.example.HMS.model.response.DoctorScheduleResponse;
import com.example.HMS.model.response.MedicalRecordResponse;
import com.example.HMS.security.CurrentUser;
import com.example.HMS.security.UserPrincipal;
import com.example.HMS.service.DoctorService;
import com.example.HMS.service.MedicalRecordService;
import com.example.HMS.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
	
	private final DoctorService doctorService;
	private final MedicalRecordService medicalRecordService;
	private final UserService userService;
	
	@GetMapping("/getDoctorSchedule")
	public List<DoctorScheduleResponse> getDoctorSchedule(@CurrentUser UserPrincipal currentUser) {
		List<DoctorScheduleResponse> responses = doctorService.getAllDoctorSchedules(currentUser);
		return responses;
	}
	
	@GetMapping("/viewAppointment")
	public List<AppointmentResposne> viewAppointment(@CurrentUser UserPrincipal currentUser){
		List<AppointmentResposne> responses = doctorService.getAllAppointments(currentUser);
		return responses;
	}
	
	@GetMapping("/patients")
	public List<UserDto> getAllPatients(){
		List<UserDto> patients = userService.getAllPatients();
		return patients;
	}
	
	@PostMapping("/addMedicalRecord")
	public ApiResponse createMedicalRecord(@CurrentUser UserPrincipal currentUser, @RequestBody MedicalRecordRequest request) {
		ApiResponse response = medicalRecordService.addMedicalRecord(currentUser, request);
		return response;
	}
	
	@GetMapping("/viewMedicalRecord")
	public List<MedicalRecordResponse> getAllMedicalRecords(){
		List<MedicalRecordResponse> responses = medicalRecordService.getAllMedicalRecords();
		return responses;
	}
}
