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
public class MediaMessage extends Message{

    @Column(columnDefinition = "TEXT")
    private String mediaTitle;

    @Column(columnDefinition = "TEXT")
    private String fileHash;

    @Column(columnDefinition = "TEXT")
    private String ref;

    private Long length;
}
