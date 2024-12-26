package com.amit.converse.chat.service.Redis;

public interface IRedisChatroomService {
    void addUserIdToChatRoom(String chatRoomId, String userId);
    void removeUserFromChatRoom(String userId);
}
