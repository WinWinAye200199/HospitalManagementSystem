package com.example.HMS.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HMS.dto.DoctorInfoDTO;
import com.example.HMS.dto.NurseInfoDTO;
import com.example.HMS.dto.StaffDto;
import com.example.HMS.dto.UserDto;
import com.example.HMS.exception.BadRequestException;
import com.example.HMS.model.request.AssignNurseRequest;
import com.example.HMS.model.request.DepartmentRequest;
import com.example.HMS.model.request.DoctorScheduleRequest;
import com.example.HMS.model.request.StaffRequest;
import com.example.HMS.model.request.UpdateStaffRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.DoctorScheduleResponse;
import com.example.HMS.service.AdminService;
import com.example.HMS.service.DepartmentService;
import com.example.HMS.service.DoctorScheduleService;
import com.example.HMS.service.DoctorService;
import com.example.HMS.service.NurseService;
import com.example.HMS.service.StaffService;
import com.example.HMS.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	private final DepartmentService departmentService;
	
	private final AdminService adminService;
	
	private final DoctorService doctorService;
	
	private final NurseService nurseService;
	
	private final StaffService staffService;
	
	private final UserService userService;
	
	private final DoctorScheduleService doctorScheduleService;
	
	@PostMapping("/createStaff")
	public ApiResponse saveStaff(@RequestBody StaffRequest request) {
		if (!request.getPassword().equals(request.getConfirmedPassword())) {
            return new ApiResponse(false, "Passwords do not match");
        }

        if (adminService.findByEmail(request.getEmail()).isPresent()) {
            return new ApiResponse(false, "Email already exists");
        }

        ApiResponse savedStaff = adminService.createStaff(request);
	
        return savedStaff;
	}
	
	@PutMapping("/updateStaff/{id}")
	public ApiResponse updateStaff(@PathVariable("id") long id,@RequestBody UpdateStaffRequest request) {
		try {
			ApiResponse response = adminService.updateStaff(id, request);
			return response;
		}catch (BadRequestException e) {
			throw new BadRequestException(e.getMessage());
		}
		
	}
	
	@GetMapping("/doctors")
	public List<DoctorInfoDTO> getAllDoctors(){
		List<DoctorInfoDTO> doctors = doctorService.getAllDoctors();
		
		return doctors;
	}
	
	@GetMapping("/nurses")
	public List<NurseInfoDTO> getAllNurses(){
		List<NurseInfoDTO> nurses = nurseService.getAllNurses();
		
		return nurses;
	}
	
	@GetMapping("/staffs")
	public List<StaffDto> getAllStaffs(){
		List<StaffDto> staffs = staffService.getAllStaffs();
		return staffs;
	}
	
	@GetMapping("/patients")
	public List<UserDto> getAllPatients(){
		List<UserDto> patients = userService.getAllPatients();
		return patients;
	}
	
	@PutMapping("/assignedNurse/{id}")
	public ApiResponse assignedNurse(@PathVariable("id") long id,@RequestBody AssignNurseRequest request) {
		ApiResponse response = doctorService.assignNurse(id, request);
		return response;
	}
	
	@DeleteMapping("/removeStaff/{id}")
	public ApiResponse removeStaff(@PathVariable("id") long id) {
		ApiResponse response = adminService.deleteStaff(id);
		return response;
	}
	
	@PostMapping("/addSchedule")
	public ApiResponse addDoctorSchedule(@RequestBody DoctorScheduleRequest request) {
		ApiResponse response = doctorScheduleService.addSchedule(request);
		return response;
	}
	
	@GetMapping("/allDoctorSchedules")
	public List<DoctorScheduleResponse> getAllDoctorSchedules() {
		List<DoctorScheduleResponse> responses = doctorScheduleService.getAllSchedules();
		return responses;
	}
	
}
