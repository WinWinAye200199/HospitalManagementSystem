package com.example.HMS.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResposne {

	private Long id;
    private String patientName;
    private String doctorName;
    private String dateTime;
    private boolean confirmed;
    private boolean cancelled;
}
