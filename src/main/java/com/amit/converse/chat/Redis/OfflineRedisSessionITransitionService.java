package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.stereotype.Service;

@Service
public class OfflineRedisSessionITransitionService extends RedisSessionITransitionService {

    public OfflineRedisSessionITransitionService(UserChatService userChatService, RedisWriteService redisWriteService) {
        super(ConnectionStatus.OFFLINE,userChatService,redisWriteService);
    }

    // Remove User Id Key from Redis
    @Override
    public void alterUser() {
        redisWriteService.removeUser(userChatService.getContextUser());
    }
}
