package com.amit.converse.chat.dto;

import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Messages.Message;
import lombok.Builder;

import java.util.List;

@Builder
public class ChatRoomData {
    List<Message> messages;
    IOnlineUsersDTO onlineUsersDTO;
}
