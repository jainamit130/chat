package com.amit.converse.chat.service.Redis.Factory;

import com.amit.converse.chat.Redis.OfflineRedisSessionITransitionService;
import com.amit.converse.chat.Redis.OnlineRedisSessionITransitionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisSessionTransitionFactory {

    @Bean
    public OnlineRedisSessionITransitionService getOnlineRedisSessionTransition() {
        return new OnlineRedisSessionITransitionService();
    }

    @Bean
    public OfflineRedisSessionITransitionService getOfflineRedisSessionTransition() {
        return new OfflineRedisSessionITransitionService();
    }
}
