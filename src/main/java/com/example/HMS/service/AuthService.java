package com.example.HMS.service;

import com.example.HMS.model.request.LoginRequest;
import com.example.HMS.model.request.ResetPasswordRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.JwtResponse;
import com.example.HMS.security.UserPrincipal;

public interface AuthService {

	JwtResponse authenticate(LoginRequest loginRequest);

	ApiResponse changePassword(UserPrincipal currentUser, String oldPassword, String newPassword);

	ApiResponse requestResetPassword(String username);

	ApiResponse resetPassword(ResetPasswordRequest request);

}
