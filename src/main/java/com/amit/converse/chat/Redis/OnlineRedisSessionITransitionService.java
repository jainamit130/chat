package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import org.springframework.stereotype.Service;

@Service
public class OnlineRedisSessionITransitionService extends RedisSessionITransitionService {

    public OnlineRedisSessionITransitionService() {
        super(ConnectionStatus.ONLINE);
    }

    // Save User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.setUser(userContext.getUserId());
    }
}
