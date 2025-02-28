package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatRoomRedisTransitionServiceFactory {

    private final Map<ChatRoomType, ChatRoomRedisTransitionService> serviceMap;

    // Constructor injection for the Map
    @Autowired
    public ChatRoomRedisTransitionServiceFactory(Map<ChatRoomType, ChatRoomRedisTransitionService> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public ChatRoomRedisTransitionService getChatRoomRedisTransitionService(ChatRoomType chatRoomType) {
        return serviceMap.getOrDefault(chatRoomType, null);
    }
}