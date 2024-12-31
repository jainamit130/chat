package com.amit.converse.chat.service.factory;

import com.amit.converse.chat.repository.ChatRoom.IChatRoomRepository;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.ClearChatService;
import com.amit.converse.chat.service.DeleteChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChatServiceFactory {
    private final IChatRoomRepository chatRoomRepository;
    private final ClearChatService clearChatService;
    private final DeleteChatService deleteChatService;

    public ChatService create() {
        return new ChatService(chatRoomRepository, clearChatService, deleteChatService);
    }
}