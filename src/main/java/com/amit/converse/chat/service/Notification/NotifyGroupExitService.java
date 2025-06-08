package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.NotificationType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("exitNotification")
public class NotifyGroupExitService extends NotifyGroupTransactionService {

    @Override
    protected String getTransactionMessage() {
        return "removed";
    }

    @Override
    protected NotificationType getNotificationType() {
        return NotificationType.EXITED_CHAT;
    }

}
