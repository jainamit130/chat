package com.amit.converse.chat.service.Redis;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisChatRoomService implements IRedisChatroomService{

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    public void addUserIdToChatRoom(String chatRoomId, String userId) {
        if (chatRoomId != null && userId != null) {
            String key = getChatRoomUserKey(chatRoomId, userId);
            redisTemplate.opsForValue().set(key, "",60,TimeUnit.SECONDS);
        }
    }

    public void removeUserFromChatRoom(String chatRoomId,String userId) {
        String key = getChatRoomUserKey(chatRoomId,userId);
        if(redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }
}
