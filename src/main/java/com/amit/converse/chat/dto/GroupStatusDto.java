package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupStatusDto {
    ChatMessage message;
    String username;
    String moderatorName;
    String id;
    StatusType type;
}
