package com.amit.converse.chat.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveData(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Instant getData(String key) {
        String timestampString = (String) redisTemplate.opsForValue().get(key);
        Instant timestamp = Instant.parse(timestampString);
        return timestamp;
    }

    public List<String> filterOnlineUsers(List<String> userIds) {
        List<String> redisKeys = userIds.stream()
                .map(userId -> "user:" + userId + ":status")
                .toList();

        List<Object> statuses = redisTemplate.opsForValue().multiGet(redisKeys);

        List<String> onlineUsers = userIds.stream()
                .filter(userId -> {
                    int index = userIds.indexOf(userId);
                    Object status = statuses.get(index);
                    return status != null && status.equals("online");
                })
                .toList();

        return onlineUsers;
    }

    public void removeData(String key) {
        redisTemplate.delete(key);
    }
}
