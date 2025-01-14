package com.amit.converse.chat.Redis;

import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import com.amit.converse.chat.service.User.GroupChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupChatRedisTransitionService extends ChatRoomRedisTransitionService {
    @Autowired
    private UserContext userContext;
    @Autowired
    private ChatContext chatContext;
    @Autowired
    private GroupChatUserService groupChatUserService;
    @Autowired
    private RedisChatRoomService redisChatRoomService;

    public IOnlineUsersDTO transitAndGetOnlineUsers() {
        transit();
        return groupChatUserService.getOnlineUsersOfChat();
    }

}
