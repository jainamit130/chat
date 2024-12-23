package com.amit.converse.chat.service.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisChatRoomService implements IRedisChatroomService{

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    public void addUserIdToChatRoom(String chatRoomId, String userId) {
        if(chatRoomId!=null)
            redisTemplate.opsForSet().add("chatRoom:" + chatRoomId + ":userIds", userId);
    }

    public void removeUserFromChatRoom(String chatRoomId, String userId) {
        if(chatRoomId==null) {
            return;
        }
        String chatRoomKey = "chatRoom:" + chatRoomId + ":userIds";

        redisTemplate.opsForSet().remove(chatRoomKey, userId);

        Long size = redisTemplate.opsForSet().size(chatRoomKey);

        if (size != null && size == 0) {
            redisTemplate.delete(chatRoomKey);
        }
    }
}
