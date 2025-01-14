package com.amit.converse.chat.Redis;

import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class ChatRoomRedisTransitionService implements ITransition {
    @Autowired
    private UserContext userContext;
    @Autowired
    private ChatContext chatContext;
    @Autowired
    private RedisChatRoomService redisChatRoomService;

    public abstract IOnlineUsersDTO transitAndGetOnlineUsers();

    @Override
    public void transit() {
        redisChatRoomService.removeUserFromChatRoom(userContext.getUserId());
        redisChatRoomService.addUserIdToChatRoom(chatContext.getChatRoom().getId(),userContext.getUserId());
    }
}
