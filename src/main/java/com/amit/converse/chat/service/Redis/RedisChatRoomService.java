package com.amit.converse.chat.service.Redis;

import java.util.List;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisChatRoomService extends RedisService {

    @Autowired
    @Lazy
    private RedisUserService redisUserService;

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

    protected void removeUserFromChatRoomFromKeys(List<String> keyValues,User user) {
        for(String keyValue:keyValues) {
            String chatRoomId = extractValue(keyValue);
            removeKeyValue(chatRoomId,user.getUserId());
        }
    }

    public void addUserToChatRoom(ChatRoom chatRoom, User user) {
        redisUserService.removeUserFromAllChatRoom(user);
        addValueToKey(chatRoom.getId(),user.getUserId());
    }
}
