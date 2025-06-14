package com.example.HMS.service.Impl;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.HMS.model.entities.Doctor;
import com.example.HMS.model.entities.DoctorSchedule;
import com.example.HMS.model.request.DoctorScheduleRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.DoctorScheduleResponse;
import com.example.HMS.repository.DoctorRepository;
import com.example.HMS.repository.DoctorScheduleRepository;
import com.example.HMS.service.DoctorScheduleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DoctorScheduleServiceImpl implements DoctorScheduleService{

	private final DoctorRepository doctorRepository;
	
	private final DoctorScheduleRepository doctorScheduleRepository;
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
	
	@Override
	public ApiResponse addSchedule(DoctorScheduleRequest request) {
		Doctor doctor = doctorRepository.findById(request.getDoctorId())
		        .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + request.getDoctorId()));
		
		LocalDate date = LocalDate.parse(request.getDate(), formatter);
		LocalTime startTime = LocalTime.parse(request.getStartTime(), formatter1);
		LocalTime endTime = LocalTime.parse(request.getEndTime(), formatter1);
		
		DoctorSchedule schedule = new DoctorSchedule();
		schedule.setDoctor(doctor);
		schedule.setDate(date);
		schedule.setStartTime(startTime);
		schedule.setEndTime(endTime);
		schedule.setAvailable(true);
		
		doctorScheduleRepository.save(schedule);
		
		return new ApiResponse(true, "Doctor's Schedule Assigned Successfully!");
	}

	@Override
	public List<DoctorScheduleResponse> getAllSchedules() {
		List<DoctorSchedule> schedules = doctorScheduleRepository.findAll();
		List<DoctorScheduleResponse> responses = new ArrayList<>();
		for(DoctorSchedule schedule : schedules ) {
			DoctorScheduleResponse response = new DoctorScheduleResponse();
			response.setDoctorName(schedule.getDoctor().getUser().getName());
			response.setDepartmentName(schedule.getDoctor().getDepartment().getName());
			response.setSpecialization(schedule.getDoctor().getSpecialization());
			response.setDate(schedule.getDate().toString());
			response.setStartTime(schedule.getStartTime().toString());
			response.setEndTime(schedule.getEndTime().toString());
			response.setAvailable(schedule.isAvailable());
			
			responses.add(response);
		}
		return responses;
	}

	@Override
	public List<DoctorScheduleResponse> getDoctorWithSpecialization(String specialization) {
	    List<Doctor> doctors = doctorRepository.findBySpecialization(specialization);
	    List<DoctorScheduleResponse> responses = new ArrayList<>();

	    for (Doctor doctor : doctors) {
	        List<DoctorSchedule> doctorSchedules = doctorScheduleRepository.findAllByDoctorId(doctor.getId());

	        for (DoctorSchedule schedule : doctorSchedules) {
	            DoctorScheduleResponse response = new DoctorScheduleResponse();
	            response.setDoctorName(schedule.getDoctor().getUser().getName());
	            response.setDate(schedule.getDate().toString());
	            response.setDepartmentName(schedule.getDoctor().getDepartment().getName());
	            response.setSpecialization(schedule.getDoctor().getSpecialization());
	            response.setStartTime(schedule.getStartTime().toString());
	            response.setEndTime(schedule.getEndTime().toString());
	            response.setAvailable(schedule.isAvailable());

	            responses.add(response);
	        }
	    }

	    return responses;
	}


}
