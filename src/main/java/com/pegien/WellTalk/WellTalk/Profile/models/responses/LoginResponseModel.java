package com.pegien.WellTalk.WellTalk.Profile.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseModel {
    private Boolean twoFactorEnabled;
    private String message;
    private String Authorization;
}