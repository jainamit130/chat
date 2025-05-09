package com.amit.converse.chat.service.Redis.Interface;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;

public interface IRedisWriteService {

    void setKey(String key);

    void addUserToChatRoom(ChatRoom chatRoom, User user);

    void removeUserFromChatRoom(User user);

    void removeUserFromChatRoom(ChatRoom chatRoom, User user);

    void setUser(User user);

    void removeUser(User user);
}
