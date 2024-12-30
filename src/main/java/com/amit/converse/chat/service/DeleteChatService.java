package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteChatService {
    @Autowired
    private ClearChatService clearChatService;
    private final ChatRoom chatRoom;
    private final String userId;

    public DeleteChatService(ChatRoom chatRoom, String userId) {
        this.chatRoom = chatRoom;
        this.userId = userId;
    }

    public void deleteChat() {
        clearChatService.clearChat();
    }
}
