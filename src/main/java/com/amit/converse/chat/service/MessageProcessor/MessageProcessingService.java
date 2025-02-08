package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessingService {
    @Autowired
    private ChatService chatService;

    @Autowired
    private DeliveryProcessingService deliveryProcessingService;

    @Autowired
    private ReadProcessingService readProcessingService;

    public final void process(ChatMessage message) {
        deliveryProcessingService.deliver(message);
        readProcessingService.read(message);
    }
}
