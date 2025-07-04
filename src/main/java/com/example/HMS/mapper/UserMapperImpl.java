package com.example.HMS.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.HMS.dto.UserDto;
import com.example.HMS.model.entities.User;
import com.example.HMS.model.request.UserRequest;
import com.example.HMS.model.response.UserResponse;

@Component
public class UserMapperImpl implements UserMapper{
	
	@Override
    public UserDto mapToDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setActive(user.isActive());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setAddress(user.getAddress());
        userDto.setRole(user.getRole()); // Assuming Role is an enum
        return userDto;
    }

    @Override
    public List<UserDto> mapToDto(List<User> users) {
        if (users == null || users.isEmpty()) {
            return List.of();
        }

        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse mapToResponse(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userDto.getId());
        userResponse.setActive(userDto.isActive());
        userResponse.setName(userDto.getName());
        userResponse.setEmail(userDto.getEmail());
        userResponse.setPhone(userDto.getPhone());
        userResponse.setAddress(userDto.getAddress());
        userResponse.setRole(userDto.getRole().name()); // Assuming Role is an enum and converting it to string
        return userResponse;
    }

    @Override
    public List<UserResponse> mapToResponse(List<UserDto> userDtos) {
        if (userDtos == null || userDtos.isEmpty()) {
            return List.of();
        }

        return userDtos.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto mapToDto(UserRequest request) {
        if (request == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setName(request.getName());
        userDto.setEmail(request.getEmail());
        userDto.setPhone(request.getPhone());
        userDto.setAddress(request.getAddress());
        return userDto;
    }
}
