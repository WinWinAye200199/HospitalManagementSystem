package com.example.HMS.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.HMS.dto.StaffDto;
import com.example.HMS.model.entities.Staff;
import com.example.HMS.model.entities.User;
import com.example.HMS.repository.StaffRepository;
import com.example.HMS.service.StaffService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StaffServiceImpl implements StaffService{
	private final StaffRepository staffRepository;

	@Override
	public void saveStaff(Staff staff) {
		Staff saveStaff = new Staff();
		saveStaff.setUser(staff.getUser());
		staffRepository.save(saveStaff);
		
	}

	@Override
	public List<StaffDto> getAllStaffs() {
		List<Staff> staffs = staffRepository.findAll();
		 return staffs.stream().map(stf -> {
		        User user = stf.getUser();
		        return new StaffDto(
		            stf.getId(),
		            user.isActive(),
		            user.getName(),
		            user.getAddress(),
		            user.getEmail(),
		            user.getPhone()
		        );
		    }).collect(Collectors.toList());
	}

}
