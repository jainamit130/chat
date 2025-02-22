package com.amit.converse.chat.service.Redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisUserService implements IRedisUserService{

    protected final RedisTemplate<String, Object> redisTemplate;

    public void setUser(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        redisTemplate.opsForValue().set(userId,"");
    }

    public void removeUser(String userId) {
        try {
            redisTemplate.delete(userId);
        } catch (Exception e) {
            System.err.println("Error deleting key: " + userId + " in redis");
            e.printStackTrace();
        }
    }
}
