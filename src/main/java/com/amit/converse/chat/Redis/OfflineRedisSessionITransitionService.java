package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.Enums.ConnectionStatus;

public class OfflineRedisSessionITransitionService extends RedisSessionITransitionService {

    public OfflineRedisSessionITransitionService() {
        super(ConnectionStatus.OFFLINE);
    }

    // Remove User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.removeUser(userContext.getUserId());
    }
}
