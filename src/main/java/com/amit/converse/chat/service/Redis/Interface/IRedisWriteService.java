package com.amit.converse.chat.service.Redis.Interface;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.User;

public interface IRedisWriteService {

    void addUserToChatRoom(User user, IChatRoom chatRoom);

    void removeUserFromChatRoom(User user);

    void setUser(User user);

    void removeUser(User user);
}
