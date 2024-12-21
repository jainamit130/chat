package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.ConnectionStatus;

public class OfflineRedisSessionTransition extends RedisSessionTransition {

    public OfflineRedisSessionTransition(String userId, String prevChatRoomId) {
        super(ConnectionStatus.OFFLINE, userId, prevChatRoomId);
    }

    // Remove ChatRoom-User Id key from Redis
    @Override
    public void alterUserToChatRoom() {
        redisService.removeUserFromChatRoom(userId,prevChatRoomId);
    }

    // Remove User Id Key from Redis
    @Override
    public void alterUser() {
        redisService.setUser(userId);
    }
}
