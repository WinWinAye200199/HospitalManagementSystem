package com.example.HMS.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.HMS.dto.NurseInfoDTO;
import com.example.HMS.exception.BadRequestException;
import com.example.HMS.exception.NotFoundException;
import com.example.HMS.model.entities.DoctorSchedule;
import com.example.HMS.model.entities.Nurse;
import com.example.HMS.model.entities.Role;
import com.example.HMS.model.entities.User;
import com.example.HMS.model.response.NurseResponse;
import com.example.HMS.repository.DoctorScheduleRepository;
import com.example.HMS.repository.NurseRepository;
import com.example.HMS.repository.UserRepository;
import com.example.HMS.security.UserPrincipal;
import com.example.HMS.service.NurseService;
import com.example.HMS.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NurseServiceImpl implements NurseService{
	
	private final NurseRepository nurseRepository;
	
	private final UserService userService;
	
	private final DoctorScheduleRepository doctorScheduleRepository;
	
	private final UserRepository userRepository;

	@Override
	public void saveNurse(Nurse nurse) {
		
		Nurse saveNurse = new Nurse();
		saveNurse.setUser(nurse.getUser());
		saveNurse.setAssignedDoctor(nurse.getAssignedDoctor());
		nurseRepository.save(saveNurse);
	}

	@Override
	public Optional<Nurse> findById(Long id) {
		Optional<Nurse> nurse = nurseRepository.findById(id);
		return nurse;
	}

	@Override
	public List<NurseInfoDTO> getAllNurses() {
		 List<Nurse> nurses = nurseRepository.findAll();
		 return nurses.stream().map(nur -> {
		        User user = nur.getUser();
		        
		        String doctorName = (nur.getAssignedDoctor() != null && nur.getAssignedDoctor().getUser() != null)
		                ? nur.getAssignedDoctor().getUser().getName()
		                : "No doctor assigned";

		        return new NurseInfoDTO(
		            nur.getId(),
		            user.isActive(),
		            user.getName(),
		            user.getAddress(),
		            user.getEmail(),
		            user.getPhone(),
		            doctorName
		        );
		    }).collect(Collectors.toList());
	}

	@Override
	public Optional<Nurse> findByName(String name) {
		Optional<User> user = userService.findByName(name);
		if(user.isPresent() && user.get().getRole() == Role.NURSE) {
			Optional<Nurse> nurse = nurseRepository.findByUserId(user.get().getId());
			return nurse;
		}
		
		throw new NotFoundException("User not Found!");
	}

	@Override
	public List<NurseResponse> getAllSchedules(UserPrincipal currentUser) {
		
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(()->new BadRequestException("User Not Found!"));
		Nurse nurse = nurseRepository.findByUserId(user.getId())
				.orElseThrow(()->new BadRequestException("Nurse Not Found!"));
		List<DoctorSchedule> schedules = doctorScheduleRepository.findAllByDoctorId(nurse.getAssignedDoctor().getId());
		List<NurseResponse> responses = new ArrayList<>();
		for(DoctorSchedule schedule : schedules) {
			NurseResponse response = new NurseResponse();
			response.setId(schedule.getId());
			response.setNurseName(nurse.getUser().getName());
			response.setDate(schedule.getDate().toString());
			response.setStartTime(schedule.getStartTime().toString());
			response.setEndTime(schedule.getEndTime().toString());
			response.setAssignedDoctorName(schedule.getDoctor().getUser().getName());
			response.setDepartmentName(schedule.getDoctor().getDepartment().getName());
			
			responses.add(response);
		}
		return responses;
	}

}
