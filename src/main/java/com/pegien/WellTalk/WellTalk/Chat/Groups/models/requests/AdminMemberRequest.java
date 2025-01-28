package com.pegien.WellTalk.WellTalk.Chat.Groups.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMemberRequest {

    private UUID groupId;

    private UUID profileId;

}
