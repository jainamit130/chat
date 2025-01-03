package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.service.MessageService.DeleteMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@AllArgsConstructor
public class ClearChatService {
    private final DeleteMessageService deleteMessageService;

    public void clearChat(ChatRoom chatRoom, String userId) {
        Map<String, Instant> userFetchStartTimeMap = chatRoom.getUserFetchStartTimeMap();
        Instant lastClearedTimestamp = userFetchStartTimeMap.getOrDefault(userId,chatRoom.getCreatedAt());
        deleteMessageService.deleteMessagesForUserFromTillNow(chatRoom,lastClearedTimestamp,userId);
    }
}
