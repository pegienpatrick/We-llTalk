package com.pegien.WellTalk.WellTalk.Chat.Messages.entity;

import com.pegien.WellTalk.WellTalk.Chat.Messages.enums.MessageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TextMessage extends Message{

    @Column(columnDefinition = "TEXT")
    private String messageContent;

    private MessageType messageType = MessageType.TEXT;

}
