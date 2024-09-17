package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class MessageProcessingService {

    private final RedisService redisService;
    private final GroupService groupService;
    private final MarkMessageService markMessageService;

    @Async
    public void processMessageAfterSave(String chatRoomId) {
        ChatRoom chatRoom = groupService.getChatRoom(chatRoomId);
        chatRoom.incrementTotalMessagesCount();
        groupService.saveChatRoom(chatRoom);
        Set<String> onlineUserIds = redisService.filterOnlineUsers(chatRoom.getUserIds());
        for (String userId : onlineUserIds) {
            markMessageService.markAllMessagesDelivered(userId);
            if (redisService.isUserInChatRoom(chatRoom.getId(), userId)) {
                markMessageService.markAllMessagesRead(chatRoomId, userId);
            }
        }
    }
}
