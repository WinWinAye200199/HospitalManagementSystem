package com.example.HMS.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeeResponse {
	
	private long id;
	
	private String name;        // e.g., "Doctor Consultation"

    private Double amount;      // e.g., 50.00

    private String description; // Optional details
}
