package com.amit.converse.chat.service.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisChatRoomService implements IRedisChatroomService{

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    public void addUserIdToChatRoom(String chatRoomId, String userId) {
        if(chatRoomId!=null)
            redisTemplate.opsForValue().set(userId,chatRoomId);
    }

    public void removeUserFromChatRoom(String userId) {
        if(redisTemplate.hasKey(userId)) {
            redisTemplate.opsForValue().set(userId,null);
        }
    }
}
