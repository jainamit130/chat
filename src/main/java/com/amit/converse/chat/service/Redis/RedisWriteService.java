package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.service.User.UserChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisWriteService implements IRedisUserService,IRedisChatroomService{
    private final IRedisUserService redisUserService;
    private final IRedisChatroomService redisChatRoomService;
    private final UserChatService userChatService;

    @Override
    public void addUserIdToChatRoom(String chatRoomId, String userId) {
        redisChatRoomService.addUserIdToChatRoom(userId,chatRoomId);
    }

    @Override
    public void removeUserFromChatRoom(String chatRoomId,String userId) {
        redisChatRoomService.removeUserFromChatRoom(chatRoomId,userId);
    }

    @Override
    public void setUser(String userId) {
        redisUserService.setUser(userId);
    }

    @Override
    public void removeUser(String userId) {
        // Remove any chatRoomId: userId that might be present in redis
        redisChatRoomService.removeUserFromChatRoom(userChatService.getContextChatRoom().getId(),userId);
        redisUserService.removeUser(userId);
    }
}
