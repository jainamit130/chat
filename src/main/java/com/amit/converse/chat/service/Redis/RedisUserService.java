package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.Interface.IRedisKeyService;
import com.amit.converse.chat.service.Redis.Interface.IRedisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

// userId:{userId}:{chatRoomId}
@Service
@Primary
public class RedisUserService extends RedisService implements IRedisKeyService, IRedisUserService {

    @Autowired
    @Lazy
    private RedisChatRoomService redisChatRoomService;

    public RedisUserService(RedisChatRoomService redisChatRoomService, RedisTemplate<String,String> redisTemplate) {
        super(redisTemplate);
        this.redisChatRoomService = redisChatRoomService;
    }

    public Boolean isKeyExisting(User user) {
        return getAllKeyValuesWithPrefix(getKey(user.getUserId())).size()>0;
    }

    @Override
    public String getPrefix() { return "userId:"; }

    @Override
    public String getKey(String userId) { return getPrefix()+userId+":"; }

    @Override
    public String getKeyValue(String userId, String chatRoomId) {
        return getKey(userId)+":"+chatRoomId;
    }

    // Set userKey without any chatRoom as value => from not existing to existing as userId:{userId}:
    // To already existing userKey remove chatRoom as value => from userId:{userId}:{chatRoomId} to userId:{userId}:
    @Override
    public void setUserKey(User user) {
        removeUserKey(user);
        setKeyValue(getKey(user.getUserId()),"");
    }

    // remove already existing userKey => from userId:{userId}:{...} to not existing
    @Override
    public void removeUserKey(User user) {
        List<String> keyValues = getAllKeyValuesWithPrefix(getKey(user.getUserId()));
        redisChatRoomService.removeUserFromChatRoomFromKeys(keyValues,user);
        removeKeyValues(keyValues);
    }

    protected void removeUserFromAllChatRoom(User user) {
        List<String> keyValues = getAllKeyValuesWithPrefix(getKey(user.getUserId()));
        redisChatRoomService.removeUserFromChatRoomFromKeys(keyValues,user);
    }

}
