package com.example.HMS.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {

	private Long id;
    private String patientName;
    private Double amount;
    private String method;
    private String paymentDate;
}
