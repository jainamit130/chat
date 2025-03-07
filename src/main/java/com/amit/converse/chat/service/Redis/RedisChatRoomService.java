package com.amit.converse.chat.service.Redis;

import java.util.List;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.Interface.IRedisChatroomService;
import com.amit.converse.chat.service.Redis.Interface.IRedisKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisChatRoomService extends RedisService implements IRedisKeyService, IRedisChatroomService {

    @Autowired
    @Lazy
    private RedisUserService redisUserService;

    public RedisChatRoomService(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getPrefix() { return "chatRoomId:"; }

    @Override
    public String getKey(String chatRoomId) { return getPrefix()+chatRoomId+":"; }

    @Override
    public String getKeyValue(String chatRoomId, String userId) {
        return getKey(chatRoomId)+":"+userId;
    }

    @Override
    public void removeUserFromChatRoomFromKeys(List<String> keyValues, User user) {
        for(String keyValue:keyValues) {
            String chatRoomId = extractValue(keyValue);
            removeKeyValue(getKeyValue(chatRoomId,user.getUserId()));
        }
    }

    @Override
    public void addUserToChatRoom(User user, IChatRoom chatRoom) {
        addValueToKey(chatRoom.getId(),user.getUserId());
    }

    @Override
    public Boolean isKeyExisting(IChatRoom chatRoom, User user) {
        return hasKeyValue(getKeyValue(chatRoom.getId(), user.getUserId()));
    }
}
