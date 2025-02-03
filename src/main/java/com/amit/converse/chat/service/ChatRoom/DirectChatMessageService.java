package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.service.MessageService.ChatMessageService;
import org.springframework.stereotype.Service;

@Service
public class DirectChatMessageService extends ChatMessageService {

    @Override
    protected void authoriseSender() {
        // all direct send messages are valid. No block feature yet.
        return;
    }
}
