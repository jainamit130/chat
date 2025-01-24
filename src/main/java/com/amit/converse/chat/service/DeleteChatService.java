package com.amit.converse.chat.service;

import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import com.amit.converse.chat.service.User.UserChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteChatService {
    private final UserContext userContext;
    private final ChatService chatService;
    private final UserChatService userChatService;
    private final RedisChatRoomService redisChatRoomService;
    private final ClearChatService clearChatService;

    public void deleteChat() {
        chatService.deleteChat();
        userChatService.deleteChat();
        redisChatRoomService.removeUserFromChatRoom(userContext.getUserId());
    }
}
