package com.amit.converse.chat.Redis;

import com.amit.converse.chat.dto.OnlineUsersDto;
import com.amit.converse.chat.service.GroupService;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class ChatRoomTransition extends RedisSession implements Transition{
    @Autowired
    private GroupService groupService;
    @Autowired
    private RedisChatRoomService redisChatRoomService;
    // current opened chatRoom's ID
    private String chatRoomId;

    public ChatRoomTransition(String userId, String chatRoomId, String prevChatRoomId) {
        super(userId, prevChatRoomId);
        this.chatRoomId = chatRoomId;
    }

    public OnlineUsersDto transitAndGetOnlineUsers() {
        transit();
        return groupService.getOnlineUsers(chatRoomId,userId);
    }

    @Override
    public void transit() {
        redisChatRoomService.removeUserFromChatRoom(prevChatRoomId,userId);
        redisChatRoomService.addUserIdToChatRoom(chatRoomId,userId);
    }
}
