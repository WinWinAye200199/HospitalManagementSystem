package com.example.HMS.service;

import java.util.List;

import com.example.HMS.model.request.DepartmentRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.DepartmentResponse;

public interface DepartmentService {

	ApiResponse saveDepartment(DepartmentRequest request);
	List<DepartmentResponse> getAllDepartments();
}
