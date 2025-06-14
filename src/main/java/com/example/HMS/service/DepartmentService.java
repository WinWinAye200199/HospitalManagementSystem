package com.example.HMS.service;

import com.example.HMS.model.request.DepartmentRequest;
import com.example.HMS.model.response.ApiResponse;

public interface DepartmentService {

	ApiResponse saveDepartment(DepartmentRequest request);
}
