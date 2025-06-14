package com.example.HMS.dto;

import java.time.LocalDate;

import com.example.HMS.model.entities.Doctor;
import com.example.HMS.model.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalRecordDto {

    private Long id;
    private User patient;
    private Doctor doctor;
    private String diagnosis;
    private LocalDate createdDate;
}
