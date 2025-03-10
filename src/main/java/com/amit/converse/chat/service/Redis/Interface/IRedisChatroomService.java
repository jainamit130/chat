package com.amit.converse.chat.service.Redis.Interface;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.User;

import java.util.List;

public interface IRedisChatroomService  {
    void removeUserFromChatRoomFromKeys(List<String> keyValues, User user);
    void addUserToChatRoom(IChatRoom chatRoom,User user);
    Boolean isKeyExisting(IChatRoom chatRoom, User user);
}
