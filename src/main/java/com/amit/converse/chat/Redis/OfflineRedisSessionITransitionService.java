package com.amit.converse.chat.Redis;

import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.service.Redis.RedisReadService;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.stereotype.Service;

@Service
public class OfflineRedisSessionITransitionService extends RedisSessionITransitionService {

    public OfflineRedisSessionITransitionService(UserChatService userChatService, RedisWriteService redisWriteService, RedisReadService redisReadService) {
        super(ConnectionStatus.INACTIVE,userChatService,redisWriteService,redisReadService);
    }

    @Override
    public boolean isTransitable() {
        return redisReadService.isUserOnline(UserContext.getUser());
    }

    // Remove User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.removeUser(UserContext.getUser());
    }
}
