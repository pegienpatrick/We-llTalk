package com.pegien.WellTalk.WellTalk.Profile.models.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor

public class ForgotPasswordSmsRequest {

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String phone;

}
