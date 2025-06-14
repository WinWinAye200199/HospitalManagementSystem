package com.example.HMS.service;

import java.util.List;

import com.example.HMS.dto.StaffDto;
import com.example.HMS.model.entities.Staff;

public interface StaffService {

	void saveStaff(Staff staff);
	
	List<StaffDto> getAllStaffs();
}
