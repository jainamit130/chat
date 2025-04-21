package com.amit.converse.chat.Redis;

import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.service.Redis.RedisReadService;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.stereotype.Service;

@Service
public class OnlineRedisSessionITransitionService extends RedisSessionITransitionService {

    public OnlineRedisSessionITransitionService(UserChatService userChatService, RedisWriteService redisWriteService, RedisReadService redisReadService) {
        super(ConnectionStatus.ACTIVE,userChatService,redisWriteService,redisReadService);
    }

    @Override
    public void commitUser() {
        redisWriteService.removeUser(UserContext.getUser());
    }

    // Save User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.setUser(UserContext.getUser());
    }
}
