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
        String timestampString =(String) redisTemplate.opsForValue().get("user:" + userId + ":timestamp");
        Instant timestamp = Instant.parse(timestampString);
        return timestamp;
    }

    public void removeTimestamp(String userId) {
        redisTemplate.opsForList().remove("user:" + userId + ":timestamp", 1, userId);
    }

    public boolean isUserInChatRoom(String chatRoomId, String userId) {
        return redisTemplate.opsForSet().isMember("chatRoom:" + chatRoomId + ":userIds", userId);
    }

    public void addUserIdToChatRoom(String chatRoomId, String userId) {
        redisTemplate.opsForSet().add("chatRoom:" + chatRoomId + ":userIds", userId);
    }


    public Set<String> filterOnlineUsers(List<String> userIds) {
        // Generate Redis keys for the user IDs
        Set<String> redisKeys = userIds.stream()
                .map(userId -> "user:" + userId + ":status")
                .collect(Collectors.toSet());

        List<Object> statuses = redisTemplate.opsForValue().multiGet(redisKeys);

        Map<String, Object> userIdToStatusMap = userIds.stream()
                .collect(Collectors.toMap(
                        userId -> userId,
                        userId -> statuses.stream()
                                .filter(status -> status != null && status.equals("online"))
                                .findFirst()
                                .orElse(null)
                ));

        Set<String> onlineUsers = userIdToStatusMap.entrySet().stream()
                .filter(entry -> "online".equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

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
