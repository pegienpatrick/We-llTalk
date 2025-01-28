package com.pegien.WellTalk.WellTalk.Profile.models.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterProfileRequest {

    @NotEmpty(message = "ProfileName cannot Be null")
    private String profileName;

    @NotEmpty(message = "Username cannot be Empty")
    private String username;

    @NotEmpty(message = "password cannot be empty")
    @Size(min = 6,message = "password must contain at least 6 characters long")
    private String password;

    private String email;

    private String phone;


}
