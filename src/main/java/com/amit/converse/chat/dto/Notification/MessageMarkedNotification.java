package com.amit.converse.chat.dto.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageMarkedNotification implements IUserNotification {
    private String chatRoomId;
    private List<String> messageIds;
}
