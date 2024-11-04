package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageUpdateResponse {
    private ChatMessage message;
    private String messageId;
    private MessageType type;
}
