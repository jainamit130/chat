package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.Messages.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatRequest {
    @NotBlank
    private ChatMessage message;
}
