package com.amit.converse.chat.service.Notification;

import org.springframework.stereotype.Service;

@Service
public class NotifyGroupExitService extends NotifyGroupTransactionService {

    @Override
    protected String getTransactionMessage() {
        return "removed";
    }
}
