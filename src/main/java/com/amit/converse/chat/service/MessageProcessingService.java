package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.OnlineStatusDto;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.OnlineStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class MessageProcessingService {

    private final ChatRoomRepository chatRoomRepository;
    private final RedisService redisService;
    private final UserService userService;
    private final WebSocketMessageService webSocketMessageService;
    private final MarkMessageService markMessageService;

    @Async
    public void processMessageAfterSave(String chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        chatRoom.incrementTotalMessagesCount();
        chatRoomRepository.save(chatRoom);
        Set<String> onlineUserIds = redisService.filterOnlineUsers(chatRoom.getUserIds());
        for (String userId : onlineUserIds) {
            markMessageService.markAllMessagesDelivered(userId);
            if (redisService.isUserInChatRoom(chatRoom.getId(), userId)) {
                markMessageService.markAllMessagesRead(chatRoomId, userId);
            }
        }
    }

    @Async
    public void sendOnlineStatusToAllChatRooms(String userId, OnlineStatus status){
        User user = userService.getUser(userId);
        OnlineStatusDto onlineStatusDto = OnlineStatusDto.builder().status(status).username(user.getUsername()).build();
        for(String chatRoomId: user.getChatRoomIds()){
            webSocketMessageService.sendOnlineStatusToGroup(chatRoomId,onlineStatusDto);
        }
        return;
    }
}
