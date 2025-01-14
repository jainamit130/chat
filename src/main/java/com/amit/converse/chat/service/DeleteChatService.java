package com.amit.converse.chat.service;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import com.amit.converse.chat.service.User.UserChatService;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteChatService {
    private final UserContext userContext;
    private final ClearChatService clearChatService;
    private final UserChatService userChatService;
    private final RedisChatRoomService redisChatRoomService;

    public void deleteChat(IChatRoom chatRoom) {
        userChatService.disconnectChat();
        clearChatService.clearChat(chatRoom);
        redisChatRoomService.removeUserFromChatRoom(userContext.getUserId());
    }
}
