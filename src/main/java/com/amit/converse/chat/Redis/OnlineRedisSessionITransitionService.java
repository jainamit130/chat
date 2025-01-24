package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.Enums.ConnectionStatus;

public class OnlineRedisSessionITransitionService extends RedisSessionITransitionService {

    public OnlineRedisSessionITransitionService(String userId) {
        super(ConnectionStatus.ONLINE);
    }

    // Save User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.setUser(userContext.getUserId());
    }
}
