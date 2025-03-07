package com.amit.converse.chat.service.Redis.Interface;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.User;

import java.util.Set;

public interface IRedisReadService {
    Boolean isUserInChatRoom(IChatRoom chatRoom, User user);
    Boolean isUserOnline(User user);
    Set<String> filterOnlineUsers(IChatRoom chatRoom);
    Set<String> filterActiveUsers(IChatRoom chatRoom);
}
