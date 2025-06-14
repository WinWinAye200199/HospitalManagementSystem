package com.example.HMS.service;

import java.util.List;
import java.util.Optional;

import com.example.HMS.dto.NurseInfoDTO;
import com.example.HMS.model.entities.Nurse;
import com.example.HMS.model.response.NurseResponse;
import com.example.HMS.security.UserPrincipal;

public interface NurseService {
	
	void saveNurse (Nurse nurse);
	Optional<Nurse> findById(Long id);
	List<NurseInfoDTO> getAllNurses();
	Optional<Nurse> findByName(String name);
	List<NurseResponse> getAllSchedules(UserPrincipal currentUser);

}
