package com.amit.converse.chat.dto.Notification;

import java.util.List;

public abstract class MessageMarkedNotification extends IUserNotification {
    protected String chatRoomId;
    protected List<String> messageIds;

    public MessageMarkedNotification(String chatRoomId, List<String> messageIds) {
        this.chatRoomId = chatRoomId;
        this.messageIds = messageIds;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public List<String> getMessageIds() {
        return messageIds;
    }

}
