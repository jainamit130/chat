package com.amit.converse.chat.service.Redis;

public interface IRedisChatroomService extends IRedisService {
    void addUserIdToChatRoom(String chatRoomId, String userId);
    void removeUserFromChatRoom(String chatRoomId,String userId);
}
