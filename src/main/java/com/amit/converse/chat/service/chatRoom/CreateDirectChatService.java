package com.amit.converse.chat.service.chatRoom;

import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.dto.CreateDirectChatRequest;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.DirectChatUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@AllArgsConstructor
public class CreateDirectChatService {

    private final DirectChatService directChatService;
    private final DirectChatUserService directChatUserService;

    public static DirectChat getNewDirectChat(User primaryUser, User counterPartUser) {
        String primaryUserId = primaryUser.getUserId();
        String counterPartUserId = counterPartUser.getUserId();
        DirectChat directChat = new DirectChat(primaryUserId, counterPartUserId, counterPartUser.getUsername());
        directChat.setDeletedForUsers(new HashSet<>() {{
            add(primaryUserId);
            add(counterPartUserId);
        }});
        return directChat;
    }

    // Returns the DirectChat userId
    public String create(String counterPartUserId,CreateDirectChatRequest directChatRequest) throws InterruptedException {
        User primaryUser = UserContext.getUser();
        User counterPartUser = directChatUserService.getUserFromRepo(counterPartUserId);
        directChatService.processCreation(primaryUser,counterPartUser,directChatRequest);
        directChatUserService.processCreation();
        return directChatService.getContextChatRoom().getId();
    }
}
