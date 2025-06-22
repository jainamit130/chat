package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.Message;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
@JsonDeserialize
public class NewChatNotification extends UserChatNotification {

    public NewChatNotification(IChatRoom chatRoom, TransactionNotification transactionNotification, List<Message> messagesHistory) {
        super(chatRoom, transactionNotification,messagesHistory);
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.NEW_CHAT;
    }
}

