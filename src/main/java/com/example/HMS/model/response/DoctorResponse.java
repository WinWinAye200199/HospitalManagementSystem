package com.example.HMS.model.response;

import com.example.HMS.model.entities.Department;
import com.example.HMS.model.entities.Nurse;
import com.example.HMS.model.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorResponse {

	private Long id;
    private String specialization;
    private User user;
    private Department department;
    private Nurse assignedNurse;
}
