package com.amit.converse.chat.service.Redis;

public interface IRedisService {
    default String getChatRoomUserKey(String chatRoomId, String userId) {
        return chatRoomId+":"+userId;
    }
}
