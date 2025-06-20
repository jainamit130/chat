package com.amit.converse.chat.service.chatRoom;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatServiceFactory {
    private final Map<ChatRoomType, ChatService> serviceMap;
    @Autowired
    private GroupChatService groupChatService;
    @Autowired
    private DirectChatService directChatService;
    @Autowired
    private SelfChatService selfChatService;

    public ChatServiceFactory() {
        this.serviceMap = new HashMap<>();
        serviceMap.put(ChatRoomType.GROUP, groupChatService);
        serviceMap.put(ChatRoomType.DIRECT, directChatService);
        serviceMap.put(ChatRoomType.SELF, selfChatService);
    }

    public ChatService getService(ChatRoomType type) {
        return serviceMap.get(type);
    }
}
