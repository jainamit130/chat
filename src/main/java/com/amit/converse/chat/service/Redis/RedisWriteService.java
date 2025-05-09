package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.Interface.IRedisWriteService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisWriteService implements IRedisWriteService {

    @Autowired
    @Lazy
    protected RedisUserService redisUserService;
    @Autowired
    protected RedisChatRoomService redisChatRoomService;

    @Override
    public void setKey(String key) {
        redisUserService.setKeyValue(key);
    }

    @Override
    public void addUserToChatRoom(ChatRoom chatRoom,User user) {
        removeUserFromChatRoom(chatRoom,user);
        redisUserService.addChatRoomToUser(user,chatRoom);
        redisChatRoomService.addUserToChatRoom(chatRoom,user);
    }

    @Override
    public void removeUserFromChatRoom(User user) {
        redisUserService.removeUserFromAllChatRoom(user);
    }

    @Override
    public void removeUserFromChatRoom(ChatRoom chatRoom, User user) {
        redisChatRoomService.removeUserFromChatRoom(chatRoom,user);
    }

    @Override
    public void setUser(User user) {
        redisUserService.setUserKey(user);
    }

    @Override
    public void removeUser(User user) {
        redisUserService.removeUserKey(user);
    }

}