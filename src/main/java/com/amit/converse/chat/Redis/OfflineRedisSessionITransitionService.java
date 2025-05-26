package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.service.Redis.RedisReadService;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserChatService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.stereotype.Service;

@Service
public class OfflineRedisSessionITransitionService extends RedisSessionITransitionService {

    public OfflineRedisSessionITransitionService(UserChatService userChatService, RedisWriteService redisWriteService, RedisReadService redisReadService) {
        super(ConnectionStatus.INACTIVE,userChatService,redisWriteService,redisReadService);
    }

    // Remove User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.removeUser(UserService.getUserContext());
    }
}
