package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.Messages.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDirectChatRequest {
    private String primaryUserId;
    @NotBlank
    private String userId;
    private ChatMessage message;
}
