package com.example.HMS.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmedAppointmentRequest {

	private Boolean confirmed;

	public Boolean getConfirmed() {
	    return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
	    this.confirmed = confirmed;
	}


}
