package com.pegien.WellTalk.WellTalk.Profile.models.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyCodeRequest {

    private String verificationCode;
}
