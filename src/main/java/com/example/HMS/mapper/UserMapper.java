package com.example.HMS.mapper;

import java.util.List;

import com.example.HMS.dto.UserDto;
import com.example.HMS.model.entities.User;
import com.example.HMS.model.request.UserRequest;
import com.example.HMS.model.response.UserResponse;

public interface UserMapper {

	UserDto mapToDto(User user);
	List<UserDto> mapToDto(List<User> users);
	UserResponse mapToResponse(UserDto userDto);
	List<UserResponse> mapToResponse(List<UserDto> userDtos);
	UserDto mapToDto(UserRequest request);
}
