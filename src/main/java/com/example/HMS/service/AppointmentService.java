package com.example.HMS.service;

import java.util.List;

import com.example.HMS.model.request.AppointmentRequest;
import com.example.HMS.model.request.ConfirmedAppointmentRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.AppointmentResposne;
import com.example.HMS.security.UserPrincipal;

public interface AppointmentService {

	ApiResponse bookAppointment(UserPrincipal currentUser, AppointmentRequest request);
	ApiResponse updateAppointment(long id, ConfirmedAppointmentRequest request);
	List<AppointmentResposne> getAllAppointments();
	ApiResponse cancelAppointmentByPatient(long id,UserPrincipal currentUser);
}
