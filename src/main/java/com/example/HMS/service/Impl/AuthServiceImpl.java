package com.example.HMS.service.Impl;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.HMS.exception.BadRequestException;
import com.example.HMS.exception.NotFoundException;
import com.example.HMS.exception.UnauthorizedException;
import com.example.HMS.model.entities.ResetToken;
import com.example.HMS.model.entities.User;
import com.example.HMS.model.request.LoginRequest;
import com.example.HMS.model.request.ResetPasswordRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.JwtResponse;
import com.example.HMS.repository.ResetTokenRepository;
import com.example.HMS.repository.UserRepository;
import com.example.HMS.security.UserPrincipal;
import com.example.HMS.service.AuthService;
import com.example.HMS.service.EmailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	private final JwtServiceImpl jwtService;
	
	private final ResetTokenRepository resetTokenRepository;

    private final EmailService emailService;

    @Override
    public JwtResponse authenticate(LoginRequest loginRequest) {
        Date expiredAt = new Date((new Date()).getTime() + 86400 * 1000);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            // Extract role
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth.equals("ROLE_ADMIN") || auth.equals("ROLE_USER")|| auth.equals("ROLE_DOCTOR") || auth.equals("ROLE_NURSE") || auth.equals("ROLE_STAFF"))
                    .findFirst()
                    .orElseThrow(() -> new UnauthorizedException("User role not found!"));

            String jwtToken = jwtService.createToken(authentication);

            return new JwtResponse("Bearer", jwtToken, expiredAt.toInstant().toString(), role);
        } catch (Exception e) {
            throw new UnauthorizedException("Username or Password is wrong!");
        }
    }

	@Override
	public ApiResponse changePassword(UserPrincipal currentUser, String oldPassword, String newPassword) {

		System.out.println(currentUser.getName());
		User foundUser = userRepository.findByName(currentUser.getUsername()).orElseThrow(
				() -> new BadRequestException("Username Not Found : username -> " + currentUser.getUsername()));
		
		String foundPassword = foundUser.getPassword();

		if (passwordEncoder.matches(oldPassword, foundPassword)) {

			foundUser.setPassword(passwordEncoder.encode(newPassword));

			userRepository.save(foundUser);
			return new ApiResponse(true, "Changed Password Successfully!");
		} else {

			throw new BadRequestException("Wrong Password!");
		}

	}


	@Override
    public ApiResponse requestResetPassword(String email) {
		 // Step 1: Find user by username
	    System.out.println("Username: " + email);
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new NotFoundException("User not found!"));
	    
	    System.out.println("User found: " + user.getName());
	    
	    // Step 2: Generate a reset code (e.g., 6-digit random code)
	    String resetCode = generateResetCode();
	    
	    // Optionally, save the code in the database for future validation (if needed)
	    ResetToken resetToken = new ResetToken(resetCode, user); 
	    resetTokenRepository.save(resetToken);

	 // Step 3: Send reset code via email
	    String emailSubject = "Reset Password";
	    String emailBody = "Dear " + user.getName() + ",\n\n"
	            + "Your password reset code is: " + resetCode + ".\n"
	            + "Please use this code to reset your password.\n\n"
	            + "Best Regards,\nSupport Team";

	    try {
	        emailService.sendEmail(user.getEmail(), emailSubject, emailBody);
	    } catch (Exception e) {
	        e.printStackTrace(); // Handle exception and log error
	    }

	    return new ApiResponse(true, "Password reset code sent to your email.");
    }
	
	private String generateResetCode() {
	    // Generate a random 6-digit code (you can modify this as needed)
	    int code = (int)(Math.random() * 900000) + 100000;
	    return String.valueOf(code);
	}


    @Override
    public ApiResponse resetPassword(ResetPasswordRequest request) {
    	// Step 1: Validate reset code
        ResetToken resetToken = resetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset code!"));

        // Step 2: Validate new passwords
        if (!request.getNewPassword().equals(request.getConfirmedPassword())) {
            throw new BadRequestException("Passwords do not match!");
        }

        // Step 3: Update user's password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Step 4: Invalidate the code (delete or mark it as used)
        resetTokenRepository.delete(resetToken);

        return new ApiResponse(true, "Password reset successfully!");
    }
}