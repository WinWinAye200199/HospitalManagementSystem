package com.example.HMS.model.response;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MedicalRecordResponse {

    private Long id;
    private String patientName;
    private String doctorName;
    private String diagnosis;
    private String createdDate;
}
