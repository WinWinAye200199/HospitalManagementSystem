package com.example.HMS.service;
import java.util.List;
import java.util.Optional;

import com.example.HMS.dto.UserDto;
import com.example.HMS.model.entities.User;
import com.example.HMS.model.request.UserRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.AppointmentResposne;
import com.example.HMS.security.UserPrincipal;
public interface UserService {
	
	ApiResponse createUser(UserRequest userRequest); 
	Optional<User> findByEmail(String email);
	List<User> getAllUsers();
	List<UserDto> getAllPatients();
	Optional<User> findByName(String name);
	List<AppointmentResposne> getAllAppointments(UserPrincipal currentUser);

}