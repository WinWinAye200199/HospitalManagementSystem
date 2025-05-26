package com.example.HMS.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.HMS.model.entities.User;
import com.example.HMS.model.request.UserRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.security.UserPrincipal;

public interface UserService {
	
	ApiResponse createUser(UserRequest userRequest); 

}