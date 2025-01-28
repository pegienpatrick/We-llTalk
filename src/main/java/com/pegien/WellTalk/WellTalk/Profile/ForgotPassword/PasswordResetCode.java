package com.pegien.WellTalk.WellTalk.Profile.ForgotPassword;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor

@Entity
public class PasswordResetCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    private UUID userId;

    private String passwordResetCode;

    private Long expiryTime= TimeUnit.MINUTES.toMillis(10);

    private Long created;

    public Boolean isUsable()
    {
        Long toExpire=created+expiryTime;
        return (new Date().before(new Date(toExpire)));
    }

}
