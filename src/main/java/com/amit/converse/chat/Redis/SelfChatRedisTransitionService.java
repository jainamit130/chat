package com.amit.converse.chat.Redis;

import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.service.User.DirectChatUserService;
import com.amit.converse.chat.service.User.SelfChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelfChatRedisTransitionService extends ChatRoomRedisTransitionService {
    @Autowired
    private SelfChatUserService selfChatUserService;

    public IOnlineUsersDTO transitAndGetOnlineUsers() {
        transit();
        return selfChatUserService.getOnlineUsersOfChat();
    }
}
