package com.amit.converse.chat.service.Redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisUserService implements IRedisUserService{

    protected final RedisTemplate<String, Object> redisTemplate;

    public void setUser(String userId) {
        redisTemplate.opsForValue().set("user:" + userId,"");
    }

    public void removeUser(String userId) {
        String key = "user:" + userId;
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            System.err.println("Error deleting key: " + key + " for user: " + userId);
            e.printStackTrace();
        }
    }
}
