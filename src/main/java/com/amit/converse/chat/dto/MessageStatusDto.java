package com.amit.converse.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageStatusDto {
    private String messageId;
    private String chatRoomId;
    private Boolean isDelivered;
}
