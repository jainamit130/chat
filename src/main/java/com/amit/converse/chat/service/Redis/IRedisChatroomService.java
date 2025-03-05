package com.amit.converse.chat.service.Redis;

public interface IRedisChatroomService  {
    void addUserToChatRoom(String chatRoomId, String userId);
    void removeUserFromChatRoom(String chatRoomId,String userId);
}
