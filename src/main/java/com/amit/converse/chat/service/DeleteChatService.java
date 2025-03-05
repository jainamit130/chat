package com.amit.converse.chat.service;

import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.MessageService.DeleteMessageService.ClearChatService;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import com.amit.converse.chat.service.User.UserChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteChatService {
    private final ChatService chatService;
    private final UserChatService userChatService;
    private final RedisChatRoomService redisChatRoomService;

    public void deleteChat() {
        ChatRoom chatRoom = chatService.getContextChatRoom();
        userChatService.deleteChat(chatRoom);
        redisChatRoomService.removeUserFromChatRoom(chatRoom,userChatService.getContextUser());
    }
}
