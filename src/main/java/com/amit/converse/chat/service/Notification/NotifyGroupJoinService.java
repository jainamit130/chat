package com.amit.converse.chat.service.Notification;

import org.springframework.stereotype.Service;

@Service
public class NotifyGroupJoinService extends NotifyGroupTransactionService {

    @Override
    protected String getTransactionMessage() {
        return "added";
    }
}
