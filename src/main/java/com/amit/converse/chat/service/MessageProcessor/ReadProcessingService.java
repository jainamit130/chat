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

    private final ChatService chatService;
    private final MarkReadService markReadService;

    @Autowired
    public ReadProcessingService( ChatService chatService, MarkReadService markReadService) {
        this.chatService = chatService;
        this.markReadService = markReadService;
    }

    @Override
    public void read(User user) {
        // all unread messages in the chatRoom must be marked read
        IChatRoom chatRoom = chatService.getContextChatRoom();
        markReadService.mark(chatRoom,user);
        markReadService.saveAllMarkedMessages();
        chatService.readMessages(user);
        markReadService.clearMarkService();
    }

    @Override
    public void read(ChatMessage message) {
        IChatRoom chatRoom = chatService.getChatRoomById(message.getChatRoomId());
        markReadService.mark(chatRoom,message);
        markReadService.saveAllMarkedMessages();
        markReadService.clearMarkService();
    }
}
