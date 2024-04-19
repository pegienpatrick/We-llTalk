package com.pegien.WellTalk.WellTalk.Chat.Messages.entity;

import com.pegien.WellTalk.WellTalk.Chat.Messages.enums.MessageDestinationType;
import com.pegien.WellTalk.WellTalk.Chat.Messages.enums.MessageType;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    private MessageType messageType;

    private Long createDate;

    private Long deliveredDate;

    private Long readDate;

    private UUID messageSourceUid;

    private MessageDestinationType destinationType;

    private UUID destinationUid;

}
