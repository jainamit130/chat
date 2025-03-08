package com.amit.converse.chat.service.Redis.Factory;

import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.service.Redis.Interface.IRedisKeyService;
import com.amit.converse.chat.service.Redis.RedisExpiration.RedisExpirationService;
import com.amit.converse.chat.service.Redis.RedisExpiration.RedisUserKeyExpirationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class RedisKeyExpirationFactory {

    @Autowired
    @Lazy
    private IRedisKeyService redisKeyService;

    @Autowired
    private RedisUserKeyExpirationService redisUserKeyExpirationService;

    @Value("${userPrefix}")
    private String userPrefix;

    @Value("${chatRoomPrefix}")
    private String chatRoomPrefix;

    public RedisExpirationService getRedisExpirationService(String key) {
        String prefix = redisKeyService.extractPrefix(key);
        if(userPrefix.equals(prefix)) return redisUserKeyExpirationService;
        throw new ConverseException("Invalid prefix : "+ prefix +"found in Redis key!");
    }
}
