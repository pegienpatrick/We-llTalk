package com.pegien.WellTalk.WellTalk.Chat.Groups.models.requests;

import com.pegien.WellTalk.WellTalk.Chat.Groups.enums.GroupMembershipType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMemberRequest {

    private UUID groupId;

    private UUID profileId;

}
