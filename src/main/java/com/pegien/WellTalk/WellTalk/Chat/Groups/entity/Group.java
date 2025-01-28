package com.pegien.WellTalk.WellTalk.Chat.Groups.entity;

import com.pegien.WellTalk.WellTalk.Chat.Groups.enums.GroupType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long createdOn;

    private Long deletedOn;

    private int members;

    private UUID creatorId;

    @Column(columnDefinition = "TEXT")
    private String inviteLink;

    private GroupType groupType;





}
