package com.amit.converse.chat.Redis;

public class OfflineSessionTransition extends SessionTransition {

    // Remove ChatRoom-User Id key from Redis
    @Override
    public void alterUserToChatRoom(String userId, String chatRoomId) {
        redisService.removeUserFromChatRoom(userId,chatRoomId);
    }

    // Remove User Id Key from Redis
    @Override
    public void alterUser(String userId) {
        redisService.setUser(userId);
    }
}
