package com.pegien.WellTalk.WellTalk.Chat.Messages.model.requests;

import com.pegien.WellTalk.WellTalk.Chat.Messages.enums.MessageDestinationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextMessageRequest {

    @NotEmpty
    @NotNull
    @NotBlank
    private String content;

    private MessageDestinationType destinationType;

    private UUID recipient;
}
