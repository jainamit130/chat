package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.Interface.IRedisWriteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisWriteService implements IRedisWriteService {

    protected final RedisUserService redisUserService;
    protected final RedisChatRoomService redisChatRoomService;

    @Override
    public void setKey(String key) {
        redisUserService.setKeyValue(key);
    }

    @Override
    public void addUserToChatRoom(IChatRoom chatRoom,User user) {
        removeUserFromChatRoom(chatRoom,user);
        redisUserService.addChatRoomToUser(user,chatRoom);
        redisChatRoomService.addUserToChatRoom(chatRoom,user);
    }

    @Override
    public void removeUserFromChatRoom(User user) {
        redisUserService.removeUserFromAllChatRoom(user);
    }

    @Override
    public void removeUserFromChatRoom(IChatRoom chatRoom,User user) {
        redisChatRoomService.removeUserFromChatRoom(chatRoom,user);
    }

    @Override
    public void setUser(User user,IChatRoom chatRoom) {
        redisUserService.setUserKey(user,chatRoom);
    }

    @Override
    public void removeUser(User user) {
        redisUserService.removeUserKey(user);
    }

}