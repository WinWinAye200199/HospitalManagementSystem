package com.example.HMS.dto;

import com.example.HMS.model.entities.Department;
import com.example.HMS.model.entities.Nurse;
import com.example.HMS.model.entities.User;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DoctorDto {

    private Long id;
    private String specialization;
    private User user;
    private Department department;
    private Nurse assignedNurse;
}
