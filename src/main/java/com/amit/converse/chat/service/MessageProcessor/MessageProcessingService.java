package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.ChatConnectService;
import com.amit.converse.chat.service.User.UserChatService;
import com.amit.converse.chat.service.chatRoom.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    private ChatConnectService chatConnectService;

    public final void process(ChatMessage message) {
        MarkingContext markingContext = new MarkingContext();
        deliveryProcessingService.deliver(message,markingContext);
        readProcessingService.read(message,markingContext);
    }

    public void processMessage(ChatRoom chatRoom, ChatMessage message) {
        chatService.processSentMessage();
        process(message);
        chatConnectService.connectChatFromUserIds(new ArrayList<>(chatRoom.getDeletedForUsers()),chatRoom);
    }
}
