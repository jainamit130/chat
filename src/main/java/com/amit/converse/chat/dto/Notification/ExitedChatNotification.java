package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.Message;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
@JsonDeserialize
public class ExitedChatNotification extends UserChatNotification {


    public ExitedChatNotification(IChatRoom chatRoom, TransactionNotification transactionNotification, List<Message> messageHistory) {
        super(chatRoom, transactionNotification,messageHistory);
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.EXITED_CHAT;
    }
}
