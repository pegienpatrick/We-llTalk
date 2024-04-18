package com.pegien.WellTalk.WellTalk.Profile.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {

    @NotEmpty(message = "ProfileName cannot Be null")
    private String profileName;

    @NotEmpty(message = "Username cannot be Empty")
    private String username;

    private String email;

    private String phone;


}
