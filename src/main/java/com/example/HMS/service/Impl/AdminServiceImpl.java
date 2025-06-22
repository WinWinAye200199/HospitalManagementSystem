package com.example.HMS.service.Impl;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.HMS.exception.BadRequestException;
import com.example.HMS.model.entities.Department;
import com.example.HMS.model.entities.Doctor;
import com.example.HMS.model.entities.Nurse;
import com.example.HMS.model.entities.Role;
import com.example.HMS.model.entities.Staff;
import com.example.HMS.model.entities.User;
import com.example.HMS.model.request.StaffRequest;
import com.example.HMS.model.request.UpdateStaffRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.repository.DepartmentRepository;
import com.example.HMS.repository.UserRepository;
import com.example.HMS.service.AdminService;
import com.example.HMS.service.DoctorService;
import com.example.HMS.service.EmailService;
import com.example.HMS.service.NurseService;
import com.example.HMS.service.StaffService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService{
	private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final DoctorService doctorService;
    private final NurseService nurseService;
    private final StaffService staffService;
    private final EmailService emailService;
    
    @Override
	public ApiResponse createStaff(StaffRequest request) {
    	
		// Validate password and confirmed password
        if (!request.getPassword().equals(request.getConfirmedPassword())) {
            throw new BadRequestException("Password and confirmed password do not match!");
        }

        // Create a new user entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());
        //Save the user
        User savedUser = userRepository.save(user);

		//Handle Doctor sign up
        if (request.getRole() == Role.DOCTOR) {
        	System.out.println("In handling doctor");
        	Doctor doctor = new Doctor();
        	doctor.setUser(savedUser);
        	doctor.setSpecialization(request.getSpecialization());
        	Department department = departmentRepository.findByName(request.getDepartment());
        	doctor.setDepartment(department);
//        	doctor.setAssignedNurse(nurseService.findById(request.getAssignedNurse()));
        	doctorService.saveDoctor(doctor);
        	
        	 // Send cancellation email
    	    String emailSubject = "Account Information";
    	    String emailBody = "Dear " + savedUser.getName() + ",\n\n"
    	            + "We created your account with email and password.You can access your account with below information. \n\n Email: " + request.getEmail()
    	            +"\n Password: "+request.getPassword()
    	            + " \n\n You can change your password after your first time login.\n\n"
    	            + "Regards,\nHospital Management Team";

    	    emailService.sendEmail(savedUser.getEmail(), emailSubject, emailBody);

        }
        
        //Handle Nurse sign up
        if (request.getRole() == Role.NURSE) {
        	Nurse nurse = new Nurse();
        	nurse.setUser(savedUser);
//        	nurse.setAssignedDoctor(doctorService.findById(request.getAssignedDoctor()));
        	nurseService.saveNurse(nurse);
        	
        	 // Send cancellation email
    	    String emailSubject = "Account Information";
    	    String emailBody = "Dear " + savedUser.getName() + ",\n\n"
    	            + "We created your account with email and password.You can access your account with below information. \n\n Email: " + request.getEmail()
    	            +"\n Password: "+request.getPassword()
    	            + " \n\n You can change your password after your first time login.\n\n"
    	            + "Regards,\nHospital Management Team";

    	    emailService.sendEmail(savedUser.getEmail(), emailSubject, emailBody);

        }
        
        if(request.getRole() == Role.STAFF) {
        	Staff staff = new Staff();
        	staff.setUser(savedUser);
        	staffService.saveStaff(staff);
        	
        	 // Send cancellation email
    	    String emailSubject = "Account Information";
    	    String emailBody = "Dear " + savedUser.getName() + ",\n\n"
    	            + "We created your account with email and password.You can access your account with below information. \n\n Email: " + request.getEmail()
    	            +"\n Password: "+request.getPassword()
    	            + " \n\n You can change your password after your first time login.\n\n"
    	            + "Regards,\nHospital Management Team";

    	    emailService.sendEmail(savedUser.getEmail(), emailSubject, emailBody);

        }
        return new ApiResponse(true, "Sign up Successfully!");
	}
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

	@Override
	public ApiResponse updateStaff(Long id, UpdateStaffRequest request) {
		User foundUser = userRepository.findById(id).orElse(null);
	    
	    if (foundUser == null) {
	        return new ApiResponse(false, "User Not Found!!");
	    }

	    // ✅ Only check and set email if it's different
	    String newEmail = request.getEmail();
	    if (newEmail != null && !newEmail.equals(foundUser.getEmail())) {
//	        User userWithEmail = userRepository.findByEmail(newEmail)
//	        		.orElseThrow(() -> new RuntimeException("Doctor not found with email: " + newEmail));
//	        if (userWithEmail != null && !userWithEmail.getId().equals(foundUser.getId())) {
//	            return new ApiResponse(false, "Email already exists!");
//	        }
	    	 // ✅ Update other fields
		    foundUser.setName(request.getName());
		    foundUser.setAddress(request.getAddress());
		    foundUser.setPhone(request.getPhone());

		    userRepository.save(foundUser);
	    }else {
	        foundUser.setEmail(newEmail);
	        // ✅ Update other fields
		    foundUser.setName(request.getName());
		    foundUser.setAddress(request.getAddress());
		    foundUser.setPhone(request.getPhone());

		    userRepository.save(foundUser);
	    }

	    // ✅ Update doctor info if applicable
	    if (foundUser.getRole() == Role.DOCTOR) {
	        doctorService.updateDoctor(id, request);
	    }

	    return new ApiResponse(true, "Update information Successfully!");
	}

	@Override
	public ApiResponse deleteStaff(Long id) {
		User foundUser = userRepository.getById(id);
		if(foundUser == null) {
			
			return new ApiResponse(false, "User Not Found!");
			
		}else {
			foundUser.setActive(false);
			
			userRepository.save(foundUser);
			
			return new ApiResponse(true, "Delete User Successfully!");
		}
	}

	

}
