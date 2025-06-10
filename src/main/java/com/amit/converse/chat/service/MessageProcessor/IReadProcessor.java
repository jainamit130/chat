package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;

public interface IReadProcessor {
    void read(ChatRoom chatRoom, User user,MarkingContext context);
    void read(ChatRoom chatRoom, ChatMessage message, MarkingContext context);
}
