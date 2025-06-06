package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.chatRoom.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ReadProcessingService implements IReadProcessor {

    @Autowired
    @Lazy
    private ChatService chatService;
    @Autowired
    @Lazy
    private MarkReadService markReadService;

    @Override
    public void read(User user,MarkingContext context) {
        // all unread messages in the chatRoom must be marked read
        IChatRoom chatRoom = chatService.getContextChatRoom();
        markReadService.mark(chatRoom,user,context);
        markReadService.saveAllMarkedMessages(context);
        chatService.readMessages(user);
    }

    @Override
    public void read(ChatMessage message, MarkingContext context) {
        IChatRoom chatRoom = chatService.getChatRoomById(message.getChatRoomId());
        markReadService.mark(chatRoom,message,context);
        markReadService.saveAllMarkedMessages(context);
    }
}
