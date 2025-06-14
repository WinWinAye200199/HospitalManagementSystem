package com.example.HMS.dto;

import java.time.LocalDate;

import com.example.HMS.model.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {

    private Long id;
    private User patient;
    private Double amount;
    private String method;
    private LocalDate paymentDate;
}
