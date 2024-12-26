package com.amit.converse.chat.service.Redis.Factory;

import com.amit.converse.chat.Redis.OfflineRedisSessionTransition;
import com.amit.converse.chat.Redis.OnlineRedisSessionTransition;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisSessionTransitionFactory {
    private final OnlineRedisSessionTransition onlineRedisSessionTransition;
    private final OfflineRedisSessionTransition offlineRedisSessionTransition;

    public OnlineRedisSessionTransition getOnlineRedisSessionTransition() {
        return onlineRedisSessionTransition;
    }

    public OfflineRedisSessionTransition getOfflineRedisSessionTransition() {
        return offlineRedisSessionTransition;
    }
}
