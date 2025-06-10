package com.amit.converse.chat.Redis;

import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import com.amit.converse.chat.service.User.DirectChatUserService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectChatRedisTransitionService extends ChatRoomRedisTransitionService {
    @Autowired
    private ChatContext chatContext;
    @Autowired
    private DirectChatUserService directChatUserService;
    @Autowired
    private RedisChatRoomService redisChatRoomService;

    public IOnlineUsersDTO transitAndGetOnlineUsers() {
        transit();
        return directChatUserService.getOnlineUsersOfChat();
    }
}
