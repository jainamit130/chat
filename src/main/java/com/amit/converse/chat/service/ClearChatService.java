package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatRoom;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class ClearChatService {
    private final ChatRoom chatRoom;
    private final String userId;

    public ClearChatService(ChatRoom chatRoom, String userId) {
        this.chatRoom = chatRoom;
        this.userId = userId;
    }

    public void clearChat() {
        Map<String, Instant> userFetchStartTimeMap = chatRoom.getUserFetchStartTimeMap();
        Instant lastClearedTimestamp = userFetchStartTimeMap.getOrDefault(userId,chatRoom.getCreatedAt());

    }
}
