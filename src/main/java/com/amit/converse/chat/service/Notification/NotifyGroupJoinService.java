package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.NotificationType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("joinNotification")
public class NotifyGroupJoinService extends NotifyGroupTransactionService {

    @Override
    protected String getTransactionMessage() {
        return "added";
    }

    @Override
    protected NotificationType getNotificationType() {
        return NotificationType.NEW_CHAT;
    }

}
