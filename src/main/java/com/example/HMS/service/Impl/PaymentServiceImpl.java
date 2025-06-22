package com.example.HMS.service.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.HMS.exception.BadRequestException;
import com.example.HMS.exception.UnauthorizedException;
import com.example.HMS.model.entities.Payment;
import com.example.HMS.model.request.PatientPaymentConfirmRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.PaymentResponse;
import com.example.HMS.repository.PaymentRepository;
import com.example.HMS.security.UserPrincipal;
import com.example.HMS.service.EmailService;
import com.example.HMS.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    
    private final EmailService emailService;

    @Override
    public ApiResponse confirmPayment(UserPrincipal currentUser, PatientPaymentConfirmRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new BadRequestException("Payment not found!"));

        if (!payment.getPatient().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Unauthorized access to payment");
        }

        if ("PAID".equals(payment.getStatus())) {
            return new ApiResponse(false, "Payment already completed.");
        }

        payment.setStatus("PAID");
        payment.setMethod(request.getPaymentMethod());
        
        payment.setTotalAmount(request.getTotalAmount());
        payment.setPaymentDate(LocalDate.now());
        paymentRepository.save(payment);

        // Optionally send email
        emailService.sendEmail(payment.getPatient().getEmail(), "Payment Confirmation",
                "Dear " + payment.getPatient().getName() + ",\n\n"
                        + "✅ Your payment of $" + payment.getTotalAmount() + " has been successfully completed.\n\n"
                        + "Thank you for choosing our hospital.\n\n"
                        + "Regards,\nHospital Management");

        return new ApiResponse(true, "Payment confirmed successfully.");
    }

    @Override
    public List<PaymentResponse> getMyPayments(UserPrincipal currentUser) {
    	List<Payment> payments = paymentRepository.findByPatientId(currentUser.getId());
    	List<PaymentResponse> responses = new ArrayList<>();
    	for(Payment payment : payments) {
    		PaymentResponse response = new PaymentResponse();
    		response.setId(payment.getId());
    		response.setPatientName(payment.getPatient().getName());
    		 // ✅ Null check for paymentDate
    	    if (payment.getPaymentDate() != null) {
    	        response.setPaymentDate(payment.getPaymentDate().toString());
    	    } else {
    	        response.setPaymentDate("N/A"); // or null, or some default value
    	    }
    		response.setMethod(payment.getMethod());
    		response.setAmount(payment.getTotalAmount());
    		
    		responses.add(response);
    		
    	}
        return responses;
    }

	@Override
	public List<PaymentResponse> getAllPayments() {
		List<Payment> payments = paymentRepository.findAll();
    	List<PaymentResponse> responses = new ArrayList<>();
    	for(Payment payment : payments) {
    		PaymentResponse response = new PaymentResponse();
    		response.setId(payment.getId());
    		response.setPatientName(payment.getPatient().getName());
    		 // ✅ Null check for paymentDate
    	    if (payment.getPaymentDate() != null) {
    	        response.setPaymentDate(payment.getPaymentDate().toString());
    	    } else {
    	        response.setPaymentDate("N/A"); // or null, or some default value
    	    }
    		response.setMethod(payment.getMethod());
    		response.setAmount(payment.getTotalAmount());
    		
    		responses.add(response);
    		
    	}
        return responses;
	}
}

