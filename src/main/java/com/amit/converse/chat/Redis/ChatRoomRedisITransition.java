package com.amit.converse.chat.Redis;

import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatRoomRedisITransition extends RedisSession implements ITransition {
    @Autowired
    private UserChatService userChatService;
    @Autowired
    private RedisChatRoomService redisChatRoomService;
    // current opened chatRoom's ID
    private String chatRoomId;

    public ChatRoomRedisITransition(String userId, String chatRoomId) {
        super(userId);
        this.chatRoomId = chatRoomId;
    }

    public IOnlineUsersDTO transitAndGetOnlineUsers() {
        transit();
        return userChatService.getOnlineUsersOfChat();
    }

    @Override
    public void transit() {
        redisChatRoomService.removeUserFromChatRoom(userId);
        redisChatRoomService.addUserIdToChatRoom(chatRoomId,userId);
    }
}
