package com.amit.converse.chat.service.Redis;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisChatRoomService implements IRedisChatroomService{

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    private boolean isValid(String chatRoomId, String userId) {
        return chatRoomId != null && userId != null;
    }

    public void addUserIdToChatRoom(String chatRoomId, String userId) {
        if(!isValid(chatRoomId,userId)) return;
        String key = getChatRoomUserKey(chatRoomId, userId);
        redisTemplate.opsForValue().set(key, "",60,TimeUnit.SECONDS);
    }

    public void removeUserFromChatRoom(String chatRoomId,String userId) {
        if(!isValid(chatRoomId,userId)) return;
        String key = getChatRoomUserKey(chatRoomId,userId);
        if(redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }
}
