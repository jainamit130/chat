package com.amit.converse.chat.service.Redis.RedisExpiration;

import com.amit.converse.chat.service.Redis.Interface.IRedisKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class RedisExpirationService {

    @Autowired
    private IRedisKeyService redisKeyService;

    public abstract void expire(String keyValue);

}
