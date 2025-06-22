package com.example.HMS.service;

import java.util.List;
import java.util.Optional;

import com.example.HMS.dto.DoctorInfoDTO;
import com.example.HMS.model.entities.Doctor;
import com.example.HMS.model.request.AssignNurseRequest;
import com.example.HMS.model.request.UpdateStaffRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.AppointmentResposne;
import com.example.HMS.model.response.DoctorInfoResponse;
import com.example.HMS.model.response.DoctorResponse;
import com.example.HMS.model.response.DoctorScheduleResponse;
import com.example.HMS.model.response.UserInfoResponse;
import com.example.HMS.security.UserPrincipal;

public interface DoctorService {

	void saveDoctor(Doctor doctor);
	Optional<Doctor> findById(Long id);
	Doctor findByUserId(Long id);
	void updateDoctor(Long id, UpdateStaffRequest request);
	List<DoctorInfoDTO> getAllDoctors(); 
	ApiResponse assignNurse(Long id, AssignNurseRequest request);
	Doctor findByName(String name);
	List<DoctorScheduleResponse> getAllDoctorSchedules(UserPrincipal currentUser);
	List<AppointmentResposne> getAllAppointments(UserPrincipal currentUser);
	List<DoctorInfoDTO> getDoctorsWithDepartment(String department);
}
