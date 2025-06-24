package com.example.HMS.service.Impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.HMS.dto.DoctorInfoDTO;
import com.example.HMS.exception.NotFoundException;
import com.example.HMS.model.entities.Appointment;
import com.example.HMS.model.entities.Department;
import com.example.HMS.model.entities.Doctor;
import com.example.HMS.model.entities.DoctorSchedule;
import com.example.HMS.model.entities.Nurse;
import com.example.HMS.model.entities.Role;
import com.example.HMS.model.entities.User;
import com.example.HMS.model.request.AssignNurseRequest;
import com.example.HMS.model.request.UpdateStaffRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.AppointmentResposne;
import com.example.HMS.model.response.DoctorInfoResponse;
import com.example.HMS.model.response.DoctorResponse;
import com.example.HMS.model.response.DoctorScheduleResponse;
import com.example.HMS.model.response.UserInfoResponse;
import com.example.HMS.model.response.UserResponse;
import com.example.HMS.repository.AppointmentRepository;
import com.example.HMS.repository.DepartmentRepository;
import com.example.HMS.repository.DoctorRepository;
import com.example.HMS.repository.DoctorScheduleRepository;
import com.example.HMS.security.UserPrincipal;
import com.example.HMS.service.DoctorService;
import com.example.HMS.service.NurseService;
import com.example.HMS.service.UserService;
import com.example.HMS.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DoctorServiceImpl implements DoctorService{
	
	private final DoctorRepository doctorRepository;
	private final DepartmentRepository departmentRepository;
	private final NurseService nurseService;
	private final UserService userService;
	private final DoctorScheduleRepository doctorScheduleRepository;
	private final AppointmentRepository appointmentRepository;

	@Override
	public void saveDoctor(Doctor doctor) {
		Doctor saveDoctor = new Doctor();
		saveDoctor.setUser(doctor.getUser());
		saveDoctor.setSpecialization(doctor.getSpecialization());
		saveDoctor.setDepartment(doctor.getDepartment());
		doctorRepository.save(saveDoctor);
		
	}

	@Override
	public Optional<Doctor> findById(Long id) {
		Optional<Doctor> doctor = doctorRepository.findById(id);
		return doctor;
	}

	@Override
	public Doctor findByUserId(Long id) {
		Doctor doctor = doctorRepository.findByUserId(id);
		return doctor;
	}

	@Override
	public void updateDoctor(Long id, UpdateStaffRequest request) {
		Doctor foundDoctor = findByUserId(id);
		if(foundDoctor == null) {
			throw new NotFoundException("Fail to update!");
		}else {
			Department department = departmentRepository.findByName(request.getDepartment());
			foundDoctor.setDepartment(department);
			foundDoctor.setSpecialization(request.getSpecialization());
			doctorRepository.save(foundDoctor);
			
		}
		
	}

	@Override
	public List<DoctorInfoDTO> getAllDoctors() {
		
		 List<Doctor> doctors = doctorRepository.findAll();
		 return doctors.stream().map(doc -> {
		        User user = doc.getUser();
		        String departmentName = (doc.getDepartment() != null)
		                ? doc.getDepartment().getName()
		                : "No department assigned";
		        
		        String nurseName = (doc.getAssignedNurse() != null && doc.getAssignedNurse().getUser() != null)
		                ? doc.getAssignedNurse().getUser().getName()
		                : "No nurse assigned";
		        return new DoctorInfoDTO(
		            doc.getId(),
		            user.isActive(),
		            user.getName(),
		            user.getEmail(),
		            user.getAddress(),
		            user.getPhone(),
		            doc.getSpecialization(),
		            departmentName,
		            nurseName
		        );
		    }).collect(Collectors.toList());
		
	}

	@Override
	public ApiResponse assignNurse(Long id, AssignNurseRequest request) {
			Doctor doctor = doctorRepository.findById(id)
		        .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

		    Nurse nurse = nurseService.findByName(request.getNurseName())
		        .orElseThrow(() -> new RuntimeException("Nurse not found with name: " + request.getNurseName()));
		    
		    if(doctor.getUser().isActive() == true) {
		    	// Set bidirectional relationship
			    doctor.setAssignedNurse(nurse);
			    nurse.setAssignedDoctor(doctor);

			    // Save only one side if CascadeType.ALL is used properly
			    doctorRepository.save(doctor); // or nurseRepository.save(nurse);

			    return new ApiResponse(true, "Nurse assigned to doctor successfully.");
		    }
		    else {
		    	return new ApiResponse(false, "Doctor has already been retired!");
		    }
		    
	}

	@Override
	public Doctor findByName(String name) {
		Optional<User> user = userService.findByName(name);
		if(user.isPresent() && user.get().getRole() == Role.DOCTOR) {
			Doctor doctor = doctorRepository.findByUserId(user.get().getId());
			return doctor;
		}
		throw new NotFoundException("User not Found!");
	}


	@Override
	public List<DoctorScheduleResponse> getAllDoctorSchedules(UserPrincipal currentUser) {
		Doctor doctor = doctorRepository.findByUserId(currentUser.getId());
		List<DoctorScheduleResponse> responses = new ArrayList<>();
		List<DoctorSchedule> schedules = doctorScheduleRepository.findAllByDoctorId(doctor.getId());
		for(DoctorSchedule schedule : schedules)
		{
			DoctorScheduleResponse response = new DoctorScheduleResponse();
			response.setDoctorName(schedule.getDoctor().getUser().getName());
			response.setSpecialization(schedule.getDoctor().getSpecialization());
			response.setDepartmentName(schedule.getDoctor().getDepartment().getName());
			response.setDate(schedule.getDate().toString());
			response.setStartTime(schedule.getStartTime().toString());
			response.setEndTime(schedule.getEndTime().toString());
			response.setAvailable(schedule.isAvailable());
			
			responses.add(response);
		}
		return responses;
	}

	@Override
	public List<AppointmentResposne> getAllAppointments(UserPrincipal currentUser) {
		Doctor doctor = doctorRepository.findByUserId(currentUser.getId());
		List<Appointment> appointments = appointmentRepository.findByDoctorId(doctor.getId());
		List<AppointmentResposne> responses = new ArrayList<>();
		
		for(Appointment appointment : appointments) {
//			if(appointment.isCancelled() == false && appointment.isConfirmed() == false) {
				AppointmentResposne response = new AppointmentResposne();
				response.setId(appointment.getId());
				response.setDoctorName(doctor.getUser().getName());
				response.setPatientName(appointment.getPatient().getName());
				response.setDateTime(appointment.getDateTime().toString());
				response.setConfirmed(appointment.isConfirmed());
				response.setCancelled(appointment.isCancelled());
				
				responses.add(response);
//			}
		}
		return responses;
	}

	@Override
	public List<DoctorInfoDTO> getDoctorsWithDepartment(String department) {
		Department foundDepartment = departmentRepository.findByName(department);
		List<Doctor> doctors = doctorRepository.findByDepartmentId(foundDepartment.getId());
		List<DoctorInfoDTO> responses = new ArrayList<>();
		for(Doctor doctor : doctors) {
			DoctorInfoDTO response = new DoctorInfoDTO();
			response.setDoctorId(doctor.getId());
			response.setFullName(doctor.getUser().getName());
			response.setEmail(doctor.getUser().getEmail());
			response.setAddress(doctor.getUser().getAddress());
			response.setPhone(doctor.getUser().getPhone());
			response.setSpecialization(doctor.getSpecialization());
			response.setDepartment(doctor.getDepartment().getName());
			response.setAssignedNurse(doctor.getAssignedNurse().getUser().getName());
			response.setActive(doctor.getUser().isActive());
			
			responses.add(response);
		}
		return responses;
	}
}
