package com.amit.converse.chat.service.Redis.Interface;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.User;

public interface IRedisWriteService {

    void addUserToChatRoom(IChatRoom chatRoom,User user);

    void removeUserFromChatRoom(User user);

    void setUser(User user,IChatRoom chatRoom);

    void removeUser(User user);
}
