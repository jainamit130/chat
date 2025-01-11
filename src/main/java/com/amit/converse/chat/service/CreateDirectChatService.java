package com.amit.converse.chat.service;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.CreateDirectChatRequest;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.service.ChatRoom.DirectChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateDirectChatService {

    private final ChatContext<IChatRoom> chatContext;
    private final UserContext userContext;
    private final UserService userService;
    private final DirectChatService directChatService;

    public String createChat(CreateDirectChatRequest directChatRequest) throws InterruptedException {
        directChatRequest.setPrimaryUserId(userContext.getUser().getUserId());
        DirectChat directChat = directChatService.createChat(directChatRequest);
        chatContext.setChatRoom(directChat);
        directChatService.sendMessage(directChatRequest.getMessage());
        directChatService.notifyChatToUser(directChatRequest.getUserId(),directChat);
        userService.joinChatRoom();
        return directChat.getId();
    }

}
