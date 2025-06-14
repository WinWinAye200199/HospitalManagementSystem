package com.example.HMS.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.HMS.model.request.AppointmentRequest;
import com.example.HMS.model.request.PatientPaymentConfirmRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.AppointmentResposne;
import com.example.HMS.model.response.DoctorScheduleResponse;
import com.example.HMS.model.response.MedicalRecordResponse;
import com.example.HMS.model.response.PaymentResponse;
import com.example.HMS.security.CurrentUser;
import com.example.HMS.security.UserPrincipal;
import com.example.HMS.service.AppointmentService;
import com.example.HMS.service.DoctorScheduleService;
import com.example.HMS.service.MedicalRecordService;
import com.example.HMS.service.PaymentService;
import com.example.HMS.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final AppointmentService appointmentService;
	
	private final DoctorScheduleService doctorScheduleService;
	
	private final PaymentService paymentService;
	
	private final MedicalRecordService medicalRecordService;
	
	private final UserService userService;

	@PostMapping("/bookAppointment")
	public ApiResponse bookAppointment(@CurrentUser UserPrincipal currentUser, @RequestBody AppointmentRequest request) {
		ApiResponse response = appointmentService.bookAppointment(currentUser, request);
		return response;
	}
	
	@PutMapping("/appointments/cancel/{id}")
	public ApiResponse cancelAppointmentByPatient(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		ApiResponse response = appointmentService.cancelAppointmentByPatient(id, currentUser);
	    return response;
	}
	
	@GetMapping("/viewAppointment")
	public List<AppointmentResposne> viewAppointment(@CurrentUser UserPrincipal currentUser){
		List<AppointmentResposne> responses = userService.getAllAppointments(currentUser);
		return responses;
	}
	
	@GetMapping("/getAllDoctorSchedules")
	public List<DoctorScheduleResponse> getAllSchedules() {
		List<DoctorScheduleResponse> responses = doctorScheduleService.getAllSchedules();
		return responses;
	}
	
	@GetMapping("/getDoctorWithSpecialization")
	public List<DoctorScheduleResponse> getDoctorWithSpecialization(@RequestParam String specialization){
		List<DoctorScheduleResponse> responses = doctorScheduleService.getDoctorWithSpecialization(specialization);
		return responses;
	}

    @PostMapping("/confirm")
    public ApiResponse confirmPayment(@CurrentUser UserPrincipal currentUser,
                                      @RequestBody PatientPaymentConfirmRequest request) {
        return paymentService.confirmPayment(currentUser, request);
    }

    @GetMapping("/my-payments")
    public List<PaymentResponse> getMyPayments(@CurrentUser UserPrincipal currentUser) {
    	List<PaymentResponse> responses = paymentService.getMyPayments(currentUser);
        return responses;
    }
    
    @GetMapping("/viewMedicalRecord")
	public List<MedicalRecordResponse> getMedicalRecords(@CurrentUser UserPrincipal currentUser){
		List<MedicalRecordResponse> responses = medicalRecordService.getMedicalRecord(currentUser);
		return responses;
	}
}
