package com.example.HMS.model.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotBlank
public class MedicalRecordRequest {

	private String patientName;
	private String date;
	private String diagnosis;
	
	private List<Long> feeIds; // multiple fees
}
