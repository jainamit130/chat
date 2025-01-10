package com.amit.converse.chat.service.ChatRoom;

import org.springframework.stereotype.Service;

@Service
public class DirectMessageService extends MessageService {

    @Override
    protected void authoriseSender() {
        // all direct send messages are valid. No block feature yet.
        return;
    }
}
