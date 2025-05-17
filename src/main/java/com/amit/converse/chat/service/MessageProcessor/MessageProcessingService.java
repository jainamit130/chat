package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.User.UserChatService;
import com.amit.converse.chat.service.chatRoom.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MessageProcessingService {

    @Autowired
    @Lazy
    private ChatService chatService;

    @Autowired
    @Lazy
    private UserChatService userChatService;

    @Autowired
    private DeliveryProcessingService deliveryProcessingService;

    @Autowired
    private ReadProcessingService readProcessingService;

    public final void process(ChatMessage message) {
        deliveryProcessingService.deliver(message);
        readProcessingService.read(message);
    }

    @Async
    public void processMessage(IChatRoom chatRoom, ChatMessage message) {
        userChatService.connectChat(new ArrayList<>(chatRoom.getDeletedForUsers()),chatRoom);
        process(message);
        chatService.processSentMessage();
    }
}
