package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.service.MessageService.DeleteMessageService.ClearChatService;
import com.amit.converse.chat.service.User.UserService;
import com.amit.converse.chat.service.chatRoom.ChatService;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteChatService {
    private final ChatService chatService;
    private final ClearChatService clearChatService;
    private final UserChatService userChatService;
    private final RedisWriteService redisWriteService;

    public void deleteChat() {
        ChatRoom chatRoom = chatService.getContextChatRoom();
        clearChatService.clearChat(chatRoom);
        userChatService.deleteChat(chatRoom);
        redisWriteService.removeUserFromChatRoom(chatRoom, UserService.getUserContext());
    }
}
