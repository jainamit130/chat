package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatRoom.IChatRoomRepository;
import com.amit.converse.chat.service.ClearChatService;
import com.amit.converse.chat.service.DeleteChatService;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class DirectChatService extends ChatService {
    private final DirectMessageService directMessageService;
    private final CreateDirectChatService createDirectChatService;
    private final UserService userService;

    public DirectChatService(ChatContext<IChatRoom> context, IChatRoomRepository chatRoomRepository, ClearChatService clearChatService, DeleteChatService deleteChatService, DirectMessageService directMessageService, UserService userService) {
        super(context, chatRoomRepository, clearChatService, deleteChatService);
        this.directMessageService = directMessageService;
        this.userService = userService;
    }

    public void sendMessage(ChatMessage message) throws InterruptedException {
        directMessageService.sendMessage(message);
    }

    public String createChat(String userId) {
        User counterPartUser = userService.getUserById(userId);
        User partUser = context.getUser();
        String directChatRoomId = createDirectChatService.create(partUser,counterPartUser);
        return directChatRoomId;
    }
}
