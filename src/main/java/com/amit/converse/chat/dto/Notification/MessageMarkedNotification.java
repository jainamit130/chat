package com.amit.converse.chat.dto.Notification;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class MessageMarkedNotification implements IUserNotification {
    private String chatRoomId;
    private List<String> messageIds;
}
