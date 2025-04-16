package com.amit.converse.chat.service.chatRoom.filfillmentService.factory;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import com.amit.converse.chat.service.chatRoom.filfillmentService.ChatRoomFulfilmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatRoomFulfilmentServiceFactory {

    private final Map<ChatRoomType, ChatRoomFulfilmentService> serviceMap;

    @Autowired
    public ChatRoomFulfilmentServiceFactory(@Lazy Map<ChatRoomType, ChatRoomFulfilmentService> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public ChatRoomFulfilmentService getFulfilmentService(ChatRoomType chatRoomType) {
        return serviceMap.getOrDefault(chatRoomType, null);
    }
}
