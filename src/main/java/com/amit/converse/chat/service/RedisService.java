package com.amit.converse.chat.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;

    public void setUserTimestamp(String userId, String timestamp) {
        redisTemplate.opsForValue().set("user:" + userId + ":timestamp", timestamp);
    }

    public Instant getUserTimestamp(String userId) {
        String timestampString = (String) redisTemplate.opsForValue().get("user:" + userId + ":timestamp");

        if (timestampString == null) {
            return null;
        }

        return Instant.parse(timestampString);
    }

    public void saveAndRemoveTimestamp(String userId) {
        Instant timestamp = getUserTimestamp(userId);
        userService.updateLastSeenOfUser(userId,timestamp);
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

        redisTemplate.opsForSet().remove(chatRoomKey,userId);

        Long size = redisTemplate.opsForSet().size(chatRoomKey);

        if (size != null && size == 0) {
            redisTemplate.delete(chatRoomKey);
        }
    }
}
