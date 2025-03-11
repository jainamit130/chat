package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageServiceFactory {
    @Autowired
    private DirectChatMessageService directChatMessageService;

    @Autowired
    private GroupChatMessageService groupChatMessageService;

    @Autowired
    @Lazy
    private ChatService chatService;

    public ChatMessageService getMessageServiceFactory() {
        ChatRoom contextChatRoom = chatService.getContextChatRoom();
        if(contextChatRoom.getChatRoomType().equals(ChatRoomType.GROUP)) {
            return groupChatMessageService;
        } else if(contextChatRoom.getChatRoomType().equals(ChatRoomType.SELF)
                 || contextChatRoom.getChatRoomType().equals(ChatRoomType.DIRECT)) {
            return directChatMessageService;
        }
        throw new ConverseException("Invalid ChatRoom type provided: "+contextChatRoom.getChatRoomType());
    }
}
