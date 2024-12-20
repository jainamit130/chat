package com.amit.converse.chat.Redis;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OnlineSessionTransition extends SessionTransition {

    // Save ChatRoom-User Id key from Redis
    @Override
    public void alterUserToChatRoom(String userId, String chatRoomId) {
        redisService.addUserIdToChatRoom(chatRoomId,userId);
    }

    // Save User Id Key from Redis
    @Override
    public void alterUser(String userId) {
        redisService.setUser(userId);
    }
}
