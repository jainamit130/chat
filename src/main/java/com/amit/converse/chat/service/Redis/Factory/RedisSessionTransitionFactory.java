package com.amit.converse.chat.service.Redis.Factory;

import com.amit.converse.chat.Redis.OfflineRedisSessionITransitionService;
import com.amit.converse.chat.Redis.OnlineRedisSessionITransitionService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class RedisSessionTransitionFactory {

    @Autowired
    @Lazy
    private UserChatService userChatService;
    @Autowired
    private RedisWriteService redisWriteService;
    @Autowired
    private RedisReadService redisReadService;

    public OnlineRedisSessionITransitionService getOnlineRedisSessionTransition() {
        return new OnlineRedisSessionITransitionService(userChatService,redisWriteService,redisReadService);
    }

    public OfflineRedisSessionITransitionService getOfflineRedisSessionTransition() {
        return new OfflineRedisSessionITransitionService(userChatService,redisWriteService,redisReadService);
    }
}
