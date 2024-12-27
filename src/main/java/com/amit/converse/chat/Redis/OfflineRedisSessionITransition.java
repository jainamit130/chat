package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.ConnectionStatus;

public class OfflineRedisSessionITransition extends RedisSessionITransition {

    public OfflineRedisSessionITransition(String userId, String prevChatRoomId) {
        super(ConnectionStatus.OFFLINE, userId, prevChatRoomId);
    }

    // Remove ChatRoom-User Id key from Redis
    @Override
    public void alterUserToChatRoom() {
        redisWriteService.removeUserFromChatRoom(userId);
    }

    // Remove User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.removeUser(userId);
    }
}
