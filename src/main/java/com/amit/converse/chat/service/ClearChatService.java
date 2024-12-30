package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.service.MessageService.DeleteMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class ClearChatService {
    private final ChatRoom chatRoom;
    private final String userId;
    @Autowired
    private DeleteMessageService deleteMessageService;

    public ClearChatService(ChatRoom chatRoom, String userId) {
        this.chatRoom = chatRoom;
        this.userId = userId;
    }

    public void clearChat() {
        Map<String, Instant> userFetchStartTimeMap = chatRoom.getUserFetchStartTimeMap();
        Instant lastClearedTimestamp = userFetchStartTimeMap.getOrDefault(userId,chatRoom.getCreatedAt());
        deleteMessageService.deleteMessagesForUserFromTillNow(lastClearedTimestamp,userId);
    }
}
