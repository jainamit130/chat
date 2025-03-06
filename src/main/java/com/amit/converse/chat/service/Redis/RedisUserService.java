package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

// userId:{userId}:{chatRoomId}
@Service
public class RedisUserService extends RedisService {

    @Autowired
    @Lazy
    private RedisChatRoomService redisChatRoomService;

    public RedisUserService(RedisChatRoomService redisChatRoomService, RedisTemplate<String,String> redisTemplate) {
        super(redisTemplate);
        this.redisChatRoomService = redisChatRoomService;
    }

    @Override
    protected String getPrefix() { return "userId:"; }

    @Override
    protected String getKey(String userId) { return getPrefix()+userId+":"; }

    @Override
    protected String getKeyValue(String userId, String chatRoomId) {
        return getKey(userId)+":"+chatRoomId;
    }

    // Set userKey without any chatRoom as value => from not existing to existing as userId:{userId}:
    // To already existing userKey remove chatRoom as value => from userId:{userId}:{chatRoomId} to userId:{userId}:
    public void setUserKey(User user) {
        removeUserKey(user);
        setKeyValue(user.getUserId(),"");
    }

    // remove already existing userKey => from userId:{userId}:{...} to not existing
    public void removeUserKey(User user) {
        List<String> keyValues = getAllKeyValuesWithPrefix(getKey(user.getUserId()));
        redisChatRoomService.removeUserFromChatRoomFromKeys(keyValues,user);
        redisTemplate.delete(keyValues);
    }

    protected void removeUserFromAllChatRoom(User user) {
        List<String> keyValues = getAllKeyValuesWithPrefix(getKey(user.getUserId()));
        redisChatRoomService.removeUserFromChatRoomFromKeys(keyValues,user);
    }

}
