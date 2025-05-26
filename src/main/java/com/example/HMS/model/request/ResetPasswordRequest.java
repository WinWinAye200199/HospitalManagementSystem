package com.example.HMS.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String token; // Reset token sent to the user's email
    private String newPassword;
    private String confirmedPassword;
}

