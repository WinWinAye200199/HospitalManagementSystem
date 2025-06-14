package com.example.HMS.dto;

import com.example.HMS.model.entities.Doctor;
import com.example.HMS.model.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NurseDto {

	private long id;
	private User user;
	private Doctor assignedDoctor;
}
