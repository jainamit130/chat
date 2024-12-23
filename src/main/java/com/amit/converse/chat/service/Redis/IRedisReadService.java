package com.amit.converse.chat.service.Redis;

import java.util.List;
import java.util.Set;

public interface IRedisReadService {
    boolean isUserInChatRoom(String chatRoomId, String userId);
    Boolean isUserOnline(String userId);
    Set<String> filterOnlineUsers(List<String> userIds);
}
