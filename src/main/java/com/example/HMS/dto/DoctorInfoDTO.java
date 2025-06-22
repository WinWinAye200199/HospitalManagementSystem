package com.example.HMS.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DoctorInfoDTO {

	private Long doctorId;
	private boolean isActive;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    private String specialization;
    private String department;
    private String assignedNurse;
    
    public DoctorInfoDTO(Long doctorId, boolean isActive, String fullName, String email, String address, String phone, String specialization, String department, String assignedNurse) {
        this.doctorId = doctorId;
        this.isActive = isActive;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.specialization = specialization;
        this.department = department;
        this.assignedNurse = assignedNurse;
    }
}
