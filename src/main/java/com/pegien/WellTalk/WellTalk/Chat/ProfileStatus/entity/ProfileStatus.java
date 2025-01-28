package com.pegien.WellTalk.WellTalk.Chat.ProfileStatus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class ProfileStatus {

    @Id
    private UUID subjectId;

    private Long lastTyping;

    private UUID typingPartner;


}
