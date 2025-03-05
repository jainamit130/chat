package com.amit.converse.chat.service.Redis;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisWriteService {


}


/*
*
*
*     public void addUserToChatRoom(String chatRoomId, String userId) {
        redisChatRoomService.addUserToChatRoom(userId,chatRoomId);
    }

    public void removeUserFromChatRoom(String chatRoomId,String userId) {
        redisChatRoomService.removeUserFromChatRoom(chatRoomId,userId);
    }

    public void setUser(String userId) {
        redisUserService.setUser(userId);
    }

    public void removeUser(String userId) {
        // In almost all cases it will be a single key userId:{userId}:{chatRoomId}
        List<String> userChatRoomKeys = redisUserService.getAllKeysWithPrefix(userId);
        for(String userChatRoomKey: userChatRoomKeys) {
            String chatRoomKey = extractChatRoomIdFromUserKey(userChatRoomKey)
        }
        redisUserService.removeUser(userId);
    }
*
* */