package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryProcessingService implements deliveryProcessor {

    @Autowired
    @Lazy
    private ChatService chatService;

    @Autowired
    private RedisReadService redisReadService;

    @Autowired
    private MarkDeliveredService markDeliveredService;

    @Override
    public void deliver(User user) {
        // all undelivered messages in all chatRooms must be marked delivered
        List<IChatRoom> chatRooms = chatService.getChatRoomsByIds(List.of(user.getChatRoomIds()), user.getUserId());
        for(IChatRoom chatRoom:chatRooms) {
            markDeliveredService.mark(chatRoom,user);
        }
        markDeliveredService.saveAllMarkedMessages();
    }

    @Override
    public void deliver(ChatMessage message) {
        IChatRoom chatRoom = chatService.getChatRoomById(message.getChatRoomId());
        markDeliveredService.mark(chatRoom,message);
        markDeliveredService.saveAllMarkedMessages();
    }
}
