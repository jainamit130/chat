package com.amit.converse.chat.service.chatRoom;

import com.amit.converse.chat.dto.CreateChatRequest;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.DirectChatUserService;
import com.amit.converse.chat.service.User.UserService;
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
        DirectChat directChat = new DirectChat(counterPartUser.getDisplayName());
        directChat.setDeletedForUsers(new HashSet<>() {{
            add(primaryUserId);
            add(counterPartUserId);
        }});
        directChat.setIsNewlyFormed(true);
        return directChat;
    }

    // Returns the DirectChat userId
    public String create(String counterPartUserId, CreateChatRequest directChatRequest) throws InterruptedException {
        User primaryUser = UserService.getUserContext();
        User counterPartUser = directChatUserService.getUserFromRepo(counterPartUserId);
        directChatService.processCreation(primaryUser,counterPartUser,directChatRequest);
        return directChatService.getContextChatRoom().getId();
    }

}
