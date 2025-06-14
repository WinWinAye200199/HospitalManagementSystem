package com.example.HMS.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class StaffDto {
	private Long staffId;
	private boolean isActive;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    
    public StaffDto(Long staffId, boolean isActive, String fullName, String email, String address, String phone) {
        this.staffId = staffId;
        this.isActive = isActive;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

}
