package com.example.HMS.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.HMS.dto.UserDto;
import com.example.HMS.exception.BadRequestException;
import com.example.HMS.exception.NotFoundException;
import com.example.HMS.mapper.UserMapper;
import com.example.HMS.model.entities.Appointment;
import com.example.HMS.model.entities.Doctor;
import com.example.HMS.model.entities.Role;
import com.example.HMS.model.entities.User;
import com.example.HMS.model.request.UserRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.AppointmentResposne;
import com.example.HMS.repository.AppointmentRepository;
import com.example.HMS.repository.DoctorRepository;
import com.example.HMS.repository.UserRepository;
import com.example.HMS.security.UserPrincipal;
import com.example.HMS.service.EmailService;
import com.example.HMS.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	
    private final BCryptPasswordEncoder passwordEncoder;
    
    private final UserMapper userMapper;
    
    private final AppointmentRepository appointmentRepository;
    
    private final DoctorRepository doctorRepository;
    
    private final EmailService emailService;

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

        user.setRole(Role.USER);
        //Save the user
        userRepository.save(user);
        
        // Send cancellation email
	    String emailSubject = "Account Information";
	    String emailBody = "Dear " + user.getName() + ",\n\n"
	            + "We created your account with email and password.You can access your account with below information. \n\n Email: " 
	    		+ userRequest.getEmail()
	            +"\n Password: "
	    		+userRequest.getPassword()
	            + " \n\n You can change your password after your first time login.\n\n"
	            + "Regards,\nHospital Management Team";

	    emailService.sendEmail(user.getEmail(), emailSubject, emailBody);

        
        

        return new ApiResponse(true, "Sign up Successfully!");
       
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public List<User> getAllUsers() {
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

	@Override
	public List<UserDto> getAllPatients() {
		List<User> users = userRepository.findByRole(Role.USER);
		List<UserDto> patients = userMapper.mapToDto(users);
		return patients;
	}

	@Override
	public Optional<User> findByName(String name) {
		Optional<User> foundUser = userRepository.findByName(name);
		return foundUser;
	}

	@Override
	public List<AppointmentResposne> getAllAppointments(UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new NotFoundException("Doctor not found with ID: " + currentUser.getId()));
		
		List<Appointment> appointments = appointmentRepository.findByPatientId(user.getId());
		List<AppointmentResposne> responses = new ArrayList<>();
		
		for(Appointment appointment : appointments) {
			
				Doctor doctor = doctorRepository.findByUserId(appointment.getDoctor().getId());
				AppointmentResposne response = new AppointmentResposne();
				response.setId(appointment.getId());
				response.setDoctorName(doctor.getUser().getName());
				response.setPatientName(appointment.getPatient().getName());
				response.setDateTime(appointment.getDateTime().toString());
				response.setConfirmed(appointment.isConfirmed());
				response.setCancelled(appointment.isCancelled());
				
				responses.add(response);
			
		}
		return responses;
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
