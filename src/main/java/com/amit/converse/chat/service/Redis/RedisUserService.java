package com.amit.converse.chat.service.Redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisUserService implements IRedisUserService{

    protected final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setUser(String userId) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");
        redisTemplate.opsForValue().set(getUserKeyPrefix(userId),"",60, TimeUnit.SECONDS);
    }

    @Override
    public void removeUserChatRoomKey(String key) {
        try {
            redisTemplate.delete(getUserKeyPrefix(key));
        } catch (Exception e) {
            System.err.println("Error deleting key: " + key + " in redis");
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAllKeysWithPrefix(String userId) {
        Set<String> keys = redisTemplate.keys(getUserKeyPrefix(userId));
        return new ArrayList<>(keys);
    }
}
