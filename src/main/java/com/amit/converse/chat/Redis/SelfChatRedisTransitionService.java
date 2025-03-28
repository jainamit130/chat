package com.amit.converse.chat.Redis;

import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.service.User.DirectChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelfChatRedisTransitionService extends ChatRoomRedisTransitionService {
    @Autowired
    private DirectChatUserService directChatUserService;

    public IOnlineUsersDTO transitAndGetOnlineUsers() {
        transit();
        return directChatUserService.getOnlineUsersOfChat();
    }
}
