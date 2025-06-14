package com.example.HMS.service;

import java.util.List;

import com.example.HMS.model.request.MedicalRecordRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.MedicalRecordResponse;
import com.example.HMS.security.UserPrincipal;

public interface MedicalRecordService {

	ApiResponse addMedicalRecord(UserPrincipal currentUser, MedicalRecordRequest request);
	List<MedicalRecordResponse> getAllMedicalRecords();
	List<MedicalRecordResponse> getMedicalRecord(UserPrincipal currentUser);
}
