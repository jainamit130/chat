package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.Messages.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDirectChatRequest {
    @NotBlank
    private ChatMessage message;
}
