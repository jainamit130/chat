package com.amit.converse.chat.service;

import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.DeleteMessageService.ClearChatService;
import com.amit.converse.chat.service.User.UserService;
import com.amit.converse.chat.service.chatRoom.ChatService;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserChatService;
import com.amit.converse.chat.service.chatRoom.ChatServiceFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteChatService<T extends ChatRoom> {
    private final ChatService chatService;
    private final ClearChatService clearChatService;
    private final UserChatService userChatService;
    private final RedisWriteService redisWriteService;

    public void deleteChat() {
        T chatRoom = (T) chatService.getContextChatRoom();
        User user = UserService.getUserContext();
        clearChatService.clearChat(chatRoom);
        userChatService.deleteChat(chatRoom, user);
        redisWriteService.removeUserFromChatRoom(chatRoom, user);
    }
}
