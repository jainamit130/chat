package com.amit.converse.chat.service;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.DeleteMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@AllArgsConstructor
public class ClearChatService {
    private final UserContext userContext;
    private final DeleteMessageService deleteMessageService;

    public void clearChat(IChatRoom chatRoom) {
        String contextUserId = userContext.getUserId();
        Map<String, Instant> userFetchStartTimeMap = chatRoom.getUserFetchStartTimeMap();
        Instant lastClearedTimestamp = userFetchStartTimeMap.getOrDefault(contextUserId,chatRoom.getCreatedAt());
        deleteMessageService.deleteMessagesForUserFromTillNow(chatRoom,lastClearedTimestamp,contextUserId);
    }
}
