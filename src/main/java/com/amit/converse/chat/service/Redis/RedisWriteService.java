package com.amit.converse.chat.service.Redis;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisWriteService implements IRedisUserService,IRedisChatroomService{
    private final RedisUserService redisUserService;
    private final RedisChatRoomService redisChatRoomService;

    @Override
    public void addUserIdToChatRoom(String chatRoomId, String userId) {
        redisChatRoomService.addUserIdToChatRoom(userId,chatRoomId);
    }

    @Override
    public void removeUserFromChatRoom(String userId) {
        redisChatRoomService.removeUserFromChatRoom(userId);
    }

    @Override
    public void setUser(String userId) {
        redisUserService.setUser(userId);
    }

    @Override
    public void removeUser(String userId) {
        redisUserService.removeUser(userId);
    }
}
