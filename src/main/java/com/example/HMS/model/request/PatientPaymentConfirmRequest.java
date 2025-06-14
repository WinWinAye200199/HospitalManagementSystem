package com.example.HMS.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientPaymentConfirmRequest {
    private Long paymentId;
    private String paymentMethod; // Optional
    private Double totalAmount;
}

