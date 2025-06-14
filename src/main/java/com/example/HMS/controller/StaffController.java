package com.example.HMS.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HMS.model.request.ConfirmedAppointmentRequest;
import com.example.HMS.model.request.DepartmentRequest;
import com.example.HMS.model.request.FeeRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.AppointmentResposne;
import com.example.HMS.model.response.FeeResponse;
import com.example.HMS.model.response.MedicalRecordResponse;
import com.example.HMS.model.response.PaymentResponse;
import com.example.HMS.service.AppointmentService;
import com.example.HMS.service.DepartmentService;
import com.example.HMS.service.FeeService;
import com.example.HMS.service.MedicalRecordService;
import com.example.HMS.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/staffs")
public class StaffController {
	
	private final MedicalRecordService medicalRecordService;
	
	private final AppointmentService appointmentService;
	
	private final FeeService feeService;
	
	private final PaymentService paymentService;
	
	private final DepartmentService departmentService;
	
	@PostMapping("/addDepartment")
	public ApiResponse saveDepartment(@RequestBody DepartmentRequest request) {
		
		ApiResponse apiResponse = departmentService.saveDepartment(request);
		
		return apiResponse;
	}
	
	@GetMapping("/viewMedicalRecord")
	public List<MedicalRecordResponse> getAllMedicalRecords(){
		List<MedicalRecordResponse> responses = medicalRecordService.getAllMedicalRecords();
		return responses;
	}
	
	@PutMapping("/confirmedAppointment/{id}")
	public ApiResponse confirmedAppointment(@PathVariable long id, @RequestBody ConfirmedAppointmentRequest request) {
		ApiResponse response = appointmentService.updateAppointment(id, request);
		return response;
	}
	
	@GetMapping("/viewAllAppointment")
	public List<AppointmentResposne> getAllAppointment(){
		List<AppointmentResposne> responses = appointmentService.getAllAppointments();
		return responses;
	}
	
	@PostMapping("/fees")
    public ApiResponse createFee(@RequestBody FeeRequest fee) {
        ApiResponse response = feeService.createFee(fee);
        return response;
    }

    @GetMapping("/fees")
    public List<FeeResponse> getAllFees() {
    	List<FeeResponse> response =  feeService.getAllFees();
        return response;
    }

    @GetMapping("/fees/{id}")
    public FeeResponse getFeeById(@PathVariable Long id) {
    	FeeResponse response = feeService.getFeeById(id);
        return response;
    }

    @PutMapping("/fees/{id}")
    public ApiResponse updateFee(@PathVariable Long id, @RequestBody FeeRequest fee) {
    	ApiResponse response = feeService.updateFee(id, fee);
        return response;
    }

    @DeleteMapping("/fees/{id}")
    public ApiResponse deleteFee(@PathVariable Long id) {
        ApiResponse response = feeService.deleteFee(id);
        return response;
    }
    
    @GetMapping("/payments")
    public List<PaymentResponse> getAllPayments() {
    	List<PaymentResponse> responses = paymentService.getAllPayments();
        return responses;
    }

}
