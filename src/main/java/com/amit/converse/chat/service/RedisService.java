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

    public boolean isUserInChatRoom(String chatRoomId, String userId) {
        return redisTemplate.opsForSet().isMember("chatRoom:" + chatRoomId + ":userIds", userId);
    }

    public void addUserIdToChatRoom(String chatRoomId, String userId) {
        redisTemplate.opsForSet().add("chatRoom:" + chatRoomId + ":userIds", userId);
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


    public void removeUserFromChatRoom(String chatRoomId, String userId) {
        String chatRoomKey = "chatRoom:" + chatRoomId + ":userIds";

        redisTemplate.opsForSet().remove(chatRoomKey, userId);

        Long size = redisTemplate.opsForSet().size(chatRoomKey);

        if (size != null && size == 0) {
            redisTemplate.delete(chatRoomKey);
        }
    }
}


// User came online
// update redis
// deliver messages
// new message -> check if chatRoom active and then read message if active

// user enters chatRoom
// update redis remove any previous active chatRoom and making this one active
// Read Messages
// new message -> check redis if chatRoom active and then read message if active

// User went offline
// update redis marking any active chatRoom inactive and update redis marking itself offline
// tell all chat Rooms that user went offline
