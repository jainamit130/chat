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

    public Boolean isUserInChatRoom(String chatRoomId, String userId) {
        return redisTemplate.hasKey(userId.toString() + " : " + chatRoomId.toString());
    }

    public Boolean isUserOnline(String userId) {
        return redisTemplate.hasKey(userId.toString());
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
