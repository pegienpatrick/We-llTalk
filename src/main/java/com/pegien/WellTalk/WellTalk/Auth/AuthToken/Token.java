package com.pegien.WellTalk.WellTalk.Auth.AuthToken;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    @Column(unique = true)
    private String value;

    private Long lastAccess;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean loggedOut;

    private Long inActiveTime= TimeUnit.HOURS.toMillis(1);

    public Boolean isUsable()
    {
        Long expiryTime=lastAccess+inActiveTime;
        return (!loggedOut&&new Date().before(new Date(expiryTime)));
    }

    private UUID profileId;

    @JsonIgnore
    private String verification;


    @JsonIgnore
    private Boolean authenticated;


}
