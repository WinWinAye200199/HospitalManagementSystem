package com.example.HMS.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.HMS.exception.BadRequestException;
import com.example.HMS.model.entities.User;
import com.example.HMS.model.request.UserRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.repository.UserRepository;
import com.example.HMS.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public ApiResponse createUser(UserRequest userRequest) {
        // Validate password and confirmed password
        if (!userRequest.getPassword().equals(userRequest.getConfirmedPassword())) {
            throw new BadRequestException("Password and confirmed password do not match!");
        }

        // Create a new user entity
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setAddress(userRequest.getAddress());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        user.setRole(userRequest.getRole());


        // Save the user
        userRepository.save(user);

        return new ApiResponse(true, "Sign up Successfully!");
    }
    
    public List<User> getAllStaff() {
        return userRepository.findAll();
    }
    
    public Optional<User> getStaffById(Long id) {
        return userRepository.findById(id);
    }
    
//    public User updateStaff(Long id, UpdateUserRequest updatedStaff) {
//        return userRepository.findById(id).map(user -> {
//        	System.out.println("Name"+updatedStaff.getName());
//            user.setName(updatedStaff.getName());
//        	user.setEmail(updatedStaff.getEmail());
//            user.setPhone(updatedStaff.getPhone());
//            user.setRole(updatedStaff.getRole());
//            return userRepository.save(user);
//        }).orElseThrow(() -> new NotFoundException("Staff not found with id " + id));
//    }

    public void deleteStaff(Long id) {
        userRepository.deleteById(id);
    }

//	@Override
//	public UserResponse getUserProfile(UserPrincipal currentUser) {
//		User foundUser = userRepository.findByName(currentUser.getName()).orElseThrow(
//				() -> new BadRequestException("Username Not Found : username -> " + currentUser.getUsername()));
//		if( foundUser == null) {
//			throw new NotFoundException("User is not existed!");
//		}
//		UserDto userDto = userMapper.mapToDto(foundUser);
//		UserResponse response = userMapper.mapToResponse(userDto);
//		return response;
//	}

	
}
