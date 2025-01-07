package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.Interface.IChatRoom;

public class NewChatNotification implements IUserNotification {
    private IChatRoom chatRoom;

    public NewChatNotification(IChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
