package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.chatRoom.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryProcessingService implements IDeliveryProcessor {

    @Autowired
    @Lazy
    private ChatService chatService;
    @Autowired
    @Lazy
    private MarkDeliveredService markDeliveredService;

    @Override
    public void deliver(User user, MarkingContext context) {
        // all undelivered messages in all chatRooms must be marked delivered
        List<ChatRoom> chatRooms = chatService.getChatRoomsByIds(new ArrayList<>(user.getChatRoomIds()), user);
        for(ChatRoom chatRoom:chatRooms) {
            markDeliveredService.mark(chatRoom,user,context);
        }
        markDeliveredService.saveAllMarkedMessages(context);
    }

    @Override
    public void deliver(ChatRoom chatRoom,ChatMessage message, MarkingContext context) {
        markDeliveredService.mark(chatRoom,message,context);
        markDeliveredService.saveAllMarkedMessages(context);
    }

}
