package com.pegien.WellTalk.WellTalk.Chat.Groups.entity;


import com.pegien.WellTalk.WellTalk.Chat.Groups.enums.GroupMembershipType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class GroupMemberShip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    private UUID groupId;

    private UUID profileId;

    private GroupMembershipType membershipType;

    private Long firstAddedOn;

    private Long lastAddedOn;

    private Long removedOn;

    private UUID addedBy;

    private UUID removedBy;
}
