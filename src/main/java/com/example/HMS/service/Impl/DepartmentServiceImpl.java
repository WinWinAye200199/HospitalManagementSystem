package com.example.HMS.service.Impl;

import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.DepartmentResponse;
import com.example.HMS.repository.DepartmentRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.HMS.model.entities.Department;
import com.example.HMS.model.request.DepartmentRequest;
import com.example.HMS.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService{
	private final DepartmentRepository departmentRepository;

	@Override
	public ApiResponse saveDepartment(DepartmentRequest request) {
		Department department = departmentRepository.findByName(request.getName());
		if(department == null) {
			Department saveDepartment = new Department();
			saveDepartment.setName(request.getName());
			departmentRepository.save(saveDepartment);
			return new ApiResponse(true, "Added Department Successfully!!");
			
		}else {
			return new ApiResponse(false, "Duplicate Department Name!!");
		}
		
	}

	@Override
	public List<DepartmentResponse> getAllDepartments() {
		List<Department> departments = departmentRepository.findAll();
		List<DepartmentResponse> responses = new ArrayList<>();
		for(Department department : departments) {
			DepartmentResponse response = new DepartmentResponse();
			response.setId(department.getId());
			response.setName(department.getName());
			
			responses.add(response);
		}
		return responses;
	}

}
