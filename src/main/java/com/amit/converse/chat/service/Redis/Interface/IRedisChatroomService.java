package com.amit.converse.chat.service.Redis.Interface;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;

import java.util.List;

public interface IRedisChatroomService  {
    void removeUserFromChatRoomKeys(List<String> keyValues, User user);

    void removeUserFromChatRoom(ChatRoom chatRoom, User user);

    void addUserToChatRoom(IChatRoom chatRoom, User user);
    Boolean isKeyExisting(IChatRoom chatRoom, User user);
}
