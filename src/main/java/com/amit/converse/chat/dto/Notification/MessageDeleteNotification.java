package com.amit.converse.chat.dto.Notification;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MessageDeleteNotification implements IChatNotification {
    private String messageId;
}
