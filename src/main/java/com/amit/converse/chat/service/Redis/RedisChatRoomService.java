package com.amit.converse.chat.service.Redis;

import java.util.List;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Notification.UserInactiveNotificationService;
import com.amit.converse.chat.service.Redis.Interface.IRedisChatroomService;
import com.amit.converse.chat.service.Redis.Interface.IRedisKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class RedisChatRoomService extends RedisService implements IRedisKeyService, IRedisChatroomService {

    @Autowired
    @Lazy
    private RedisUserService redisUserService;

    @Autowired
    private UserInactiveNotificationService userInactiveNotificationService;

    @Override
    public String getPrefix() { return "chatRoomId:"; }

    @Override
    public String getKey(String chatRoomId) { return getPrefix()+chatRoomId+":"; }

    @Override
    public String getKeyValue(String chatRoomId, String userId) {
        return getKey(chatRoomId)+userId;
    }

    @Override
    public void removeUserFromChatRoomFromKeys(List<String> keyValues, User user) {
        for(String keyValue:keyValues) {
            try {
                String chatRoomId = extractValue(keyValue);
                removeKeyValue(getKeyValue(chatRoomId,user.getUserId()));
            } catch (ConverseException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    @Override
    public void addUserToChatRoom(IChatRoom chatRoom,User user) {
        setKeyValue(getKeyValue(chatRoom.getId(),user.getUserId()));
    }

    @Override
    public Boolean isKeyExisting(IChatRoom chatRoom, User user) {
        return hasKeyValue(getKeyValue(chatRoom.getId(), user.getUserId()));
    }
}
