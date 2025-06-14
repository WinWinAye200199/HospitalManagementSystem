package com.example.HMS.service;

import java.util.Optional;

import com.example.HMS.model.entities.User;
import com.example.HMS.model.request.StaffRequest;
import com.example.HMS.model.request.UpdateStaffRequest;
import com.example.HMS.model.response.ApiResponse;

public interface AdminService {

	ApiResponse createStaff (StaffRequest request);

	Optional<User> findByEmail(String email);
	
	ApiResponse updateStaff(Long id, UpdateStaffRequest request);
	
	ApiResponse deleteStaff(Long id);
}
