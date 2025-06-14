package com.example.HMS.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentRequest {

    private Long doctorId;
    private String dateTime;
}
