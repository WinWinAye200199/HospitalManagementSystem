package com.example.HMS.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HMS.model.response.MedicalRecordResponse;
import com.example.HMS.model.response.NurseResponse;
import com.example.HMS.security.CurrentUser;
import com.example.HMS.security.UserPrincipal;
import com.example.HMS.service.MedicalRecordService;
import com.example.HMS.service.NurseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/nurses")
public class NurseController {
	
	private final NurseService nurseService;
	
	private final MedicalRecordService medicalRecordService;
	
	@GetMapping("/getNurseSchedules")
	public List<NurseResponse> getAllSchedules(@CurrentUser UserPrincipal currentUser){
		List<NurseResponse> responses = nurseService.getAllSchedules(currentUser);
		return responses;
	}

	@GetMapping("/viewMedicalRecord")
	public List<MedicalRecordResponse> getAllMedicalRecords(){
		List<MedicalRecordResponse> responses = medicalRecordService.getAllMedicalRecords();
		return responses;
	}
}
