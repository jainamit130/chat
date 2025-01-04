package com.amit.converse.chat.dto.Notification;

import java.util.List;

public class MessageMarkedNotification implements INotification{
    private String senderId;
    private List<String> messageIds;
}
