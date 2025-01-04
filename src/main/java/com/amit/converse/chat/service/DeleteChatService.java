package com.amit.converse.chat.service;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteChatService {
    private final ClearChatService clearChatService;
    private final UserService userService;
    private final RedisChatRoomService redisChatRoomService;

    public void deleteChat(IChatRoom chatRoom, String userId) {
        userService.groupJoinedOrLeft(userId,chatRoom.getId(),false);
        clearChatService.clearChat(chatRoom,userId);
        redisChatRoomService.removeUserFromChatRoom(userId);
    }
}
