package com.example.HMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NurseInfoDTO {

	private Long nurseId;
	private boolean isActive;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    
    private String assignedDoctor;
    
    public NurseInfoDTO(Long nurseId, boolean isActive, String fullName, String email, String address, String phone, String assignedDoctor) {
        this.nurseId = nurseId;
        this.isActive = isActive;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.assignedDoctor = assignedDoctor;
    }
}
