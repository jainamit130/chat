package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.Interface.IChatRoom;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // Active Users - meaning users online and inside the chatRoom
    public Set<String> filterActiveUsers(IChatRoom chatRoom) {
        List<String> onlineUserIds = new ArrayList<>(filterOnlineUsers(chatRoom));
        Set<String> activeUsers = new HashSet<>();

        for (String userId : onlineUserIds) {
            if (isUserInChatRoom(chatRoom.getId(),userId)) {
                activeUsers.add(userId);
            }
        }

        return activeUsers;
    }

    public Set<String> filterOnlineUsers(IChatRoom chatRoom) {
        Set<String> onlineUsers = new HashSet<>();

        for (String userId : chatRoom.getUserIds()) {
            if (isUserOnline(userId)) {
                onlineUsers.add(userId);
            }
        }

        return onlineUsers;
    }
}
