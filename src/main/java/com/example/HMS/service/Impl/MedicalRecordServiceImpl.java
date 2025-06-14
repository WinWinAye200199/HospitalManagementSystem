package com.example.HMS.service.Impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.HMS.exception.BadRequestException;
import com.example.HMS.model.entities.Doctor;
import com.example.HMS.model.entities.Fee;
import com.example.HMS.model.entities.User;
import com.example.HMS.model.entities.MedicalRecord;
import com.example.HMS.model.entities.Payment;
import com.example.HMS.model.request.MedicalRecordRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.MedicalRecordResponse;
import com.example.HMS.repository.DoctorRepository;
import com.example.HMS.repository.FeeRepository;
import com.example.HMS.repository.MedicalRecordRepository;
import com.example.HMS.repository.PaymentRepository;
import com.example.HMS.repository.UserRepository;
import com.example.HMS.security.UserPrincipal;
import com.example.HMS.service.EmailService;
import com.example.HMS.service.MedicalRecordService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService{
	
	private final DoctorRepository doctorRepository;
	
	private final MedicalRecordRepository medicalRecordRepository;
	
	private final UserRepository userRepository;
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private final EmailService emailService;
	
	private final PaymentRepository paymentRepository;
	
	private final FeeRepository feeRepository;

	@Override
	public ApiResponse addMedicalRecord(UserPrincipal currentUser, MedicalRecordRequest request) {
		Doctor doctor = doctorRepository.findByUserId(currentUser.getId());
		User foundUser = userRepository.findByName(request.getPatientName())
				.orElseThrow(()-> new BadRequestException("Patient Not Found!"));
		
		LocalDate date = LocalDate.parse(request.getDate(), formatter);
		if(doctor != null) {
			MedicalRecord record = new MedicalRecord();
			record.setDoctor(doctor);
			record.setPatient(foundUser);
			record.setCreatedDate(date);
			record.setDiagnosis(request.getDiagnosis());
			
			medicalRecordRepository.save(record);
			
			// Fetch all fees by IDs from request
	        List<Fee> fees = feeRepository.findAllById(request.getFeeIds());
	        if (fees.isEmpty()) {
	            throw new BadRequestException("No valid fees found!");
	        }

	        // Calculate total amount
	        double totalAmount = fees.stream()
	                                 .mapToDouble(Fee::getAmount)
	                                 .sum();

	        // Create payment record
	        Payment payment = new Payment();
	        payment.setPatient(foundUser);
	        payment.setMedicalRecord(record);
	        payment.setTotalAmount(totalAmount);
	        payment.setStatus("PENDING");
	        payment.setPaymentDate(null);
	        paymentRepository.save(payment);

	        // Send email with payment details
	        StringBuilder feeDetails = new StringBuilder();
	        for (Fee fee : fees) {
	            feeDetails.append(fee.getDescription()).append(": $").append(fee.getAmount()).append("\n");
	        }

	        String emailSubject = "Medical Record Created - Payment Details";
	        String emailBody = "Dear " + foundUser.getName() + ",\n\n"
	                + "Your medical record has been created with the following fees:\n"
	                + feeDetails.toString() + "\n"
	                + "Total Amount: $" + totalAmount + "\n\n"
	                + "Please make your payment at your earliest convenience.\n\n"
	                + "Best regards,\nHospital Management Team";

	        emailService.sendEmail(foundUser.getEmail(), emailSubject, emailBody);

	        return new ApiResponse(true, "Medical Record and Payment created successfully!");
		}
		return new ApiResponse(false, "Doctor not found!");
	}

	@Override
	public List<MedicalRecordResponse> getAllMedicalRecords() {
		List<MedicalRecord> records = medicalRecordRepository.findAll();
		List<MedicalRecordResponse> responses = new ArrayList<>();
		for(MedicalRecord record : records) {
			MedicalRecordResponse response = new MedicalRecordResponse();
			response.setId(record.getId());
			response.setDoctorName(record.getDoctor().getUser().getName());
			response.setPatientName(record.getPatient().getName());
			response.setCreatedDate(record.getCreatedDate().toString());
			response.setDiagnosis(record.getDiagnosis());
			
			responses.add(response);
		}
		return responses;
	}

	@Override
	public List<MedicalRecordResponse> getMedicalRecord(UserPrincipal currentUser) {
		List<MedicalRecord> records = medicalRecordRepository.findByPatientId(currentUser.getId());
		List<MedicalRecordResponse> responses = new ArrayList<>();
		for(MedicalRecord record : records) {
			MedicalRecordResponse response = new MedicalRecordResponse();
			response.setId(record.getId());
			response.setDoctorName(record.getDoctor().getUser().getName());
			response.setPatientName(record.getPatient().getName());
			response.setCreatedDate(record.getCreatedDate().toString());
			response.setDiagnosis(record.getDiagnosis());
			
			responses.add(response);
		}
		return responses;
	}

}
