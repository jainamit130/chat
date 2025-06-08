package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.Interface.IChatRoom;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonDeserialize
public class ExitedChatNotification extends UserChatNotification {


    public ExitedChatNotification(IChatRoom chatRoom, TransactionNotification transactionNotification) {
        super(chatRoom, transactionNotification);
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.EXITED_CHAT;
    }
}
