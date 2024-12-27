package com.amit.converse.chat.service.Redis.Factory;

import com.amit.converse.chat.Redis.OfflineRedisSessionITransition;
import com.amit.converse.chat.Redis.OnlineRedisSessionITransition;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisSessionTransitionFactory {
    private final OnlineRedisSessionITransition onlineRedisSessionTransition;
    private final OfflineRedisSessionITransition offlineRedisSessionTransition;

    public OnlineRedisSessionITransition getOnlineRedisSessionTransition() {
        return onlineRedisSessionTransition;
    }

    public OfflineRedisSessionITransition getOfflineRedisSessionTransition() {
        return offlineRedisSessionTransition;
    }
}
