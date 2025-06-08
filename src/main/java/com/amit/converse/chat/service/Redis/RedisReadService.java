package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.Interface.IRedisReadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class RedisReadService implements IRedisReadService {

    protected final RedisUserService redisUserService;
    protected final RedisChatRoomService redisChatRoomService;

    public Boolean isUserInChatRoom(IChatRoom chatRoom, User user) {
        return redisChatRoomService.isKeyExisting(chatRoom,user);
    }

    public ConnectionStatus getConnectionStatus(User user) {
        if(isUserOnline(user)) return ConnectionStatus.ACTIVE;
        return ConnectionStatus.INACTIVE;
    }

    public Boolean isUserOnline(User user) {
        return redisUserService.isKeyExisting(user);
    }

    // Active Users - meaning users online and inside the chatRoom
    public Set<String> filterActiveUsers(IChatRoom chatRoom) {
        List<String> onlineUserIds = new ArrayList<>(filterOnlineUsers(chatRoom));
        Set<String> activeUsers = new HashSet<>();

        for (String userId : onlineUserIds) {
            if (isUserInChatRoom(chatRoom, User.builder().userId(userId).build())) {
                activeUsers.add(userId);
            }
        }

        return activeUsers;
    }

    public Set<String> filterActiveUsers(IChatRoom chatRoom,List<String> onlineUserIds) {
        Set<String> activeUsers = new HashSet<>();

        for (String userId : onlineUserIds) {
            if (isUserInChatRoom(chatRoom,User.builder().userId(userId).build())) {
                activeUsers.add(userId);
            }
        }

        return activeUsers;
    }

    public Set<String> filterOnlineUsers(IChatRoom chatRoom) {
        Set<String> onlineUsers = new HashSet<>();

        for (String userId : chatRoom.getAllUserIds()) {
            if (isUserOnline(User.builder().userId(userId).build())) {
                onlineUsers.add(userId);
            }
        }

        return onlineUsers;
    }
}
