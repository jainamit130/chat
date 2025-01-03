package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.Enums.ConnectionStatus;

public class OfflineRedisSessionITransition extends RedisSessionITransition {

    public OfflineRedisSessionITransition(String userId) {
        super(ConnectionStatus.OFFLINE, userId);
    }

    // Remove User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.removeUser(userId);
    }
}
