package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

// userId:{userId}:{chatRoomId}
@Service
public class RedisUserService extends RedisService {

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
        setKeyValue(user.getUserId(),"");
    }

    public void removeChatRoomFromUserKey(ChatRoom chatRoom) {

    }

    // remove already existing userKey => from userId:{userId}:{...} to not existing
    public void removeUserKey(User user) {
        List<String> keyValues = getAllKeyValuesWithPrefix(getKey(user.getUserId()));
        for(String keyValue:keyValues) {
            String chatRoomId = extractValue(keyValue);
            redisChatRoomService.removeKeyValue(chatRoomId,user.getUserId());
            removeKeyValue(user.getUserId(), chatRoomId);
        }
    }

}
