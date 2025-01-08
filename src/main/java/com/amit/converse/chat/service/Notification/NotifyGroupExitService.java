package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatContext;
import org.springframework.stereotype.Service;

@Service
public class NotifyGroupExitService extends NotifyGroupTransactionService {

    public NotifyGroupExitService(ChatContext<ITransactable> chatContext, ChatNotificationService chatNotificationService) {
        super(chatContext, chatNotificationService);
    }

    @Override
    protected String getTransactionMessage() {
        return "removed";
    }
}
