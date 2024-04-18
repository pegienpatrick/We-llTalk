package com.pegien.WellTalk.WellTalk.Profile.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotEmpty(message = "username cannot be empty")
    private String username;

    @NotEmpty(message = "we need the password Reset code please")
    private String resetCode;

    @Size(min = 6,message = "password must contain at least 6 characters long")
    private String newPassword;

    private String confirmPassword;
}
