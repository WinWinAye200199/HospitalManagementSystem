package com.example.HMS.service;

import java.util.List;

import com.example.HMS.model.request.PatientPaymentConfirmRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.PaymentResponse;
import com.example.HMS.security.UserPrincipal;

public interface PaymentService {

	ApiResponse confirmPayment(UserPrincipal currentUser, PatientPaymentConfirmRequest request);
    List<PaymentResponse> getMyPayments(UserPrincipal currentUser);
    List<PaymentResponse> getAllPayments();
}
