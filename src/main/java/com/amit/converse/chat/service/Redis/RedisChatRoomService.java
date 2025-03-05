package com.amit.converse.chat.service.Redis;

import java.util.concurrent.TimeUnit;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisChatRoomService extends RedisService {

    public RedisChatRoomService(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getPrefix() { return "chatRoomId:"; }

    @Override
    protected String getKey(String chatRoomId) { return getPrefix()+chatRoomId+":"; }

    @Override
    protected String getKeyValue(String chatRoomId, String userId) {
        return getKey(chatRoomId)+":"+userId;
    }

    public void removeUserFromChatRoom(ChatRoom chatRoom, User user) {
        
    }

    public void addUserToChatRoom(ChatRoom chatRoom, User user) {
    }
}
