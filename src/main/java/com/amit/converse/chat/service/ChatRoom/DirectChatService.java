package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatRoom.IChatRoomRepository;
import com.amit.converse.chat.repository.ChatRoom.IDirectChatRepository;
import com.amit.converse.chat.service.ClearChatService;
import com.amit.converse.chat.service.DeleteChatService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.stereotype.Service;

@Service
public class DirectChatService extends ChatService<DirectChat> {
    private final IDirectChatRepository directChatRepository;
    private final DirectMessageService directMessageService;
    private final CreateDirectChatService createDirectChatService;
    private final UserService userService;

    public DirectChatService(ChatContext context, IChatRoomRepository chatRoomRepository, ClearChatService clearChatService, DeleteChatService deleteChatService, IDirectChatRepository directChatRepository, DirectMessageService directMessageService, CreateDirectChatService createDirectChatService, UserService userService) {
        super(context, chatRoomRepository, clearChatService, deleteChatService);
        this.directChatRepository = directChatRepository;
        this.directMessageService = directMessageService;
        this.createDirectChatService = createDirectChatService;
        this.userService = userService;
    }

    public DirectChat saveDirectChatToDB(DirectChat directChat) {
        return directChatRepository.save(directChat);
    }

    public void sendMessage(ChatMessage message) throws InterruptedException {
        directMessageService.sendMessage(message);
    }

    public String createChat(String userId) {
        User counterPartUser = userService.getUserById(userId);
        User partUser = context.getUser();
        DirectChat savedDirectChat = saveDirectChatToDB(createDirectChatService.create(partUser.getUserId(),counterPartUser.getUserId()));
        updateChatRoomContext(savedDirectChat);
        return savedDirectChat.getId();
    }
}
