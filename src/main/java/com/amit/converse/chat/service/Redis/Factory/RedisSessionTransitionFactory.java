package com.amit.converse.chat.service.Redis.Factory;

import com.amit.converse.chat.Redis.OfflineRedisSessionITransitionService;
import com.amit.converse.chat.Redis.OnlineRedisSessionITransitionService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisSessionTransitionFactory {
    private final OnlineRedisSessionITransitionService onlineRedisSessionTransition;
    private final OfflineRedisSessionITransitionService offlineRedisSessionTransition;

    public OnlineRedisSessionITransitionService getOnlineRedisSessionTransition() {
        return onlineRedisSessionTransition;
    }

    public OfflineRedisSessionITransitionService getOfflineRedisSessionTransition() {
        return offlineRedisSessionTransition;
    }
}
