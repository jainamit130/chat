package com.amit.converse.chat.service.Redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class RedisReadService implements IRedisReadService {

    protected final RedisTemplate<String, Object> redisTemplate;

    public boolean isUserInChatRoom(String chatRoomId, String userId) {
        return redisTemplate.opsForSet().isMember("chatRoom:" + chatRoomId + ":userIds", userId);
    }

    public Boolean isUserOnline(String userId) {
        String redisKey = "user:" + userId;
        return redisTemplate.hasKey(redisKey);
    }

    public Set<String> filterOnlineUsers(List<String> userIds) {
        Set<String> onlineUsers = new HashSet<>();

        for (String userId : userIds) {
            if (isUserOnline(userId)) {
                onlineUsers.add(userId);
            }
        }

        return onlineUsers;
    }
}
