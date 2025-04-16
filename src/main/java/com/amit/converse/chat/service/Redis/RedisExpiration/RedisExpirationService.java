package com.amit.converse.chat.service.Redis.RedisExpiration;

import com.amit.converse.chat.service.Redis.Interface.IRedisKeyService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public abstract class RedisExpirationService {

    @Autowired
    protected IRedisKeyService redisKeyService;

    public abstract void expire(String keyValue);

}
