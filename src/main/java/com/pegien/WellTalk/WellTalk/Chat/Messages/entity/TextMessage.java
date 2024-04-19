package com.pegien.WellTalk.WellTalk.Chat.Messages.entity;

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

}
