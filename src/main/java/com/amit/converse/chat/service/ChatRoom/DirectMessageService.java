package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.service.MessageProcessingService;
import com.amit.converse.chat.service.MessageService.SaveMessageService;
import com.amit.converse.chat.service.Notification.ChatNotificationService;
import org.springframework.stereotype.Service;

@Service
public class DirectMessageService extends SendMessageService {

    private final ChatContext chatContext;

    public DirectMessageService(ChatContext chatContext, MessageProcessingService messageProcessingService, SaveMessageService saveMessageService, ChatService chatService, ChatNotificationService notificationService, ChatContext chatContext1) {
        super(chatContext, messageProcessingService, saveMessageService, chatService, notificationService);
        this.chatContext = chatContext1;
    }

    @Override
    protected void authoriseSender() {
        // all direct send messages are valid. No block feature yet.
        return;
    }
}
