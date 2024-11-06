package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.StatusType;
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
    String id;
    StatusType type;
}
