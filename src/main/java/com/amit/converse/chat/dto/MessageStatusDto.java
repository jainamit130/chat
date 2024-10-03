package com.amit.converse.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageStatusDto {
    private List<String> messageIds;
    private String chatRoomId;
    private Boolean isDelivered;
}
