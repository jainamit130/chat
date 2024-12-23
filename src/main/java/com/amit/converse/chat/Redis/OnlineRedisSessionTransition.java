package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.ConnectionStatus;

public class OnlineRedisSessionTransition extends RedisSessionTransition {

    public OnlineRedisSessionTransition(String userId, String prevChatRoomId) {
        super(ConnectionStatus.ONLINE, userId, prevChatRoomId);
    }

    // Save ChatRoom-User Id key from Redis
    @Override
    public void alterUserToChatRoom() {
        redisWriteService.addUserIdToChatRoom(prevChatRoomId,userId);
    }

    // Save User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.setUser(userId);
    }
}
