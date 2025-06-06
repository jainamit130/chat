package com.amit.converse.chat.service.Notification;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("exitNotification")
public class NotifyGroupExitService extends NotifyGroupTransactionService {

    @Override
    protected String getTransactionMessage() {
        return "removed";
    }

}
