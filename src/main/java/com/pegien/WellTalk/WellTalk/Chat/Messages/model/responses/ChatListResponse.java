package com.pegien.WellTalk.WellTalk.Chat.Messages.model.responses;

import com.pegien.WellTalk.WellTalk.Chat.Messages.entity.Message;
import com.pegien.WellTalk.WellTalk.Chat.Messages.enums.MessageDestinationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatListResponse {

    private String username;

    private String profileName;

    private Long lastConv;

    private Long lastSeen;

    private String status;

    private UUID partnerId;

    private int counter;

    private MessageDestinationType partnerType;

    private Message lastMessage;


}
