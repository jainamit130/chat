package com.amit.converse.chat.service.ChatRoom.FilfillmentService;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatRoomFulfilmentServiceFactory {

    private final Map<ChatRoomType, ChatRoomFulfilmentService> serviceMap;

    // Constructor injection for the Map
    @Autowired
    public ChatRoomFulfilmentServiceFactory(Map<ChatRoomType, ChatRoomFulfilmentService> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public ChatRoomFulfilmentService getFulfilmentService(ChatRoomType chatRoomType) {
        return serviceMap.getOrDefault(chatRoomType, null);
    }
}
