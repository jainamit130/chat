package com.amit.converse.chat.Redis;

import com.amit.converse.chat.dto.OnlineUsersDto;
import com.amit.converse.chat.service.GroupService;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatRoomTransition extends RedisSession implements Transition{
    @Autowired
    private GroupService groupService;
    @Autowired
    private RedisChatRoomService redisChatRoomService;
    // current opened chatRoom's ID
    private String chatRoomId;

    public ChatRoomTransition(String userId, String chatRoomId) {
        super(userId);
        this.chatRoomId = chatRoomId;
    }

    public OnlineUsersDto transitAndGetOnlineUsers() {
        transit();
        return groupService.getOnlineUsers(chatRoomId,userId);
    }

    @Override
    public void transit() {
        redisChatRoomService.removeUserFromChatRoom(userId);
        redisChatRoomService.addUserIdToChatRoom(chatRoomId,userId);
    }
}
