package com.amit.converse.chat.service.Notification;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("joinNotification")
public class NotifyGroupJoinService extends NotifyGroupTransactionService {

    @Override
    protected String getTransactionMessage() {
        return "added";
    }

}
