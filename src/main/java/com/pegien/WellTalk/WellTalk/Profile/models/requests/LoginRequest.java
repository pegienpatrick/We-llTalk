package com.pegien.WellTalk.WellTalk.Profile.models.requests;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor

public class LoginRequest {


    @NotNull
    @NotEmpty(message = "username cannot be empty")
    private String username;

    @NotNull
    @NotEmpty(message = "password cannot be empty")
    private String password;

}
