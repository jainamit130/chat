package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.Interface.IChatRoom;

import java.util.List;
import java.util.Set;

public interface IRedisReadService {
    Boolean isUserInChatRoom(String chatRoomId, String userId);
    Boolean isUserOnline(String userId);
    Set<String> filterOnlineUsers(IChatRoom chatRoom);
    Set<String> filterActiveUsers(IChatRoom chatRoom);
}
