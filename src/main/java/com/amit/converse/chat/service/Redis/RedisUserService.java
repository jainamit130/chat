package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.Interface.IRedisKeyService;
import com.amit.converse.chat.service.Redis.Interface.IRedisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

// userId:{userId}:{chatRoomId}
@Service
@Primary
public class RedisUserService extends RedisService implements IRedisKeyService, IRedisUserService {

    @Autowired
    @Lazy
    private RedisChatRoomService redisChatRoomService;

    public Boolean isKeyExisting(User user) {
        return getAllKeyValuesWithPrefix(getKey(user.getUserId())).size()>0;
    }

    @Override
    public String getPrefix() { return "userId:"; }

    @Override
    public String getKey(String userId) { return getPrefix()+userId+":"; }

    @Override
    public String getKeyValue(String userId, String chatRoomId) {
        return getKey(userId)+chatRoomId;
    }

    // Set userKey without any chatRoom as value => from not existing to existing as userId:{userId}:
    // To already existing userKey remove chatRoom as value => from userId:{userId}:{chatRoomId} to userId:{userId}:
    @Override
    public void setUserKey(User user) {
        removeUserKey(user);
        setKeyValue(getKey(user.getUserId()));
    }

    @Override
    public void setUserKey(User user,IChatRoom chatRoom) {
        String chatRoomId = chatRoom==null?"":chatRoom.getId();
        removeUserKey(user);
        setKeyValue(getKeyValue(user.getUserId(),chatRoomId));
    }

    // remove already existing userKey => from userId:{userId}:{...} to not existing
    @Override
    public void removeUserKey(User user) {
        List<String> keyValues = getAllKeyValuesWithPrefix(getKey(user.getUserId()));
        redisChatRoomService.removeUserFromChatRoomKeys(keyValues,user);
        removeKeyValues(keyValues);
    }

    protected void removeUserFromAllChatRoom(User user) {
        List<String> keyValues = getAllKeyValuesWithPrefix(getKey(user.getUserId()));
        redisChatRoomService.removeUserFromChatRoomKeys(keyValues,user);
    }

    public void addChatRoomToUser(User user, IChatRoom chatRoom) {
        if(chatRoom.getId()!=null && chatRoom.getId()!="")  setUserKey(user,chatRoom);
    }

    public String getActiveChatRoom(User user) {
        List<String> keyValues = getAllKeyValuesWithPrefix(getKey(user.getUserId()));
        if(keyValues.size()==0) return "";
        try {
            if(keyValues.size()==1) return extractValue(keyValues.getFirst());
            throw new ConverseException("Multiple Active chatRooms found!");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return "";
    }
}
