package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.ConnectionStatus;

public class OnlineRedisSessionTransition extends RedisSessionTransition {

    public OnlineRedisSessionTransition(String userId) {
        super(ConnectionStatus.ONLINE, userId);
    }

    // Save User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.setUser(userId);
    }
}
