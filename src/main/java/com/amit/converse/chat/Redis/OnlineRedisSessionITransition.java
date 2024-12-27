package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.ConnectionStatus;

public class OnlineRedisSessionITransition extends RedisSessionITransition {

    public OnlineRedisSessionITransition(String userId) {
        super(ConnectionStatus.ONLINE, userId);
    }

    // Save User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.setUser(userId);
    }
}
