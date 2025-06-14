package com.example.HMS.dto;

import java.time.LocalDateTime;

import com.example.HMS.model.entities.Doctor;
import com.example.HMS.model.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDto {

	private Long id;
    private User patient;
    private Doctor doctor;
    private LocalDateTime dateTime;
    private boolean confirmed;
    private boolean cancelled;
}
