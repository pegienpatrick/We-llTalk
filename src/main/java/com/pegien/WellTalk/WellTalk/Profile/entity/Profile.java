package com.pegien.WellTalk.WellTalk.Profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    private String username;

    private String profileName;

    private String email;

    private String phone;

    @ToString.Exclude
    @JsonIgnore
    private String password;



}
