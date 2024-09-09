package com.amit.converse.chat.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveUserTimestamp(String userId, String timestamp) {
        redisTemplate.opsForValue().set("user:" + userId + ":timestamp", timestamp);
    }

    public Instant getUserTimestamp(String userId) {
        String timestampString = (String) redisTemplate.opsForValue().get("user:" + userId + ":timestamp");

        if (timestampString == null) {
            return null;
        }

        return Instant.parse(timestampString);
    }

    public void removeTimestamp(String userId) {
        String key = "user:" + userId + ":timestamp";
        redisTemplate.delete(key);
    }


    public boolean isUserInChatRoom(String chatRoomId, String userId) {
        return redisTemplate.opsForSet().isMember("chatRoom:" + chatRoomId + ":userIds", userId);
    }

    public void addUserIdToChatRoom(String chatRoomId, String userId) {
        redisTemplate.opsForSet().add("chatRoom:" + chatRoomId + ":userIds", userId);
    }


    public Set<String> filterOnlineUsers(List<String> userIds) {
        Set<String> redisKeys = userIds.stream()
                .map(userId -> "user:" + userId + ":timestamp")
                .collect(Collectors.toSet());

        Set<String> onlineUsers = new HashSet<>();

        for (String userId : userIds) {
            String redisKey = "user:" + userId + ":timestamp";

            Boolean exists = redisTemplate.hasKey(redisKey);

            if (Boolean.TRUE.equals(exists)) {
                onlineUsers.add(userId);
            }
        }

        return onlineUsers;
    }


    public void removeUserFromChatRoom(String chatRoomId, String userId) {
        String chatRoomKey = "chatRoom:" + chatRoomId + ":userIds";

        redisTemplate.opsForList().remove(chatRoomKey, 1, userId);

        Long size = redisTemplate.opsForList().size(chatRoomKey);

        if (size != null && size == 0) {
            redisTemplate.delete(chatRoomKey);
        }
    }
}
