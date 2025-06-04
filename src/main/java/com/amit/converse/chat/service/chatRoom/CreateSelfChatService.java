package com.amit.converse.chat.service.chatRoom;

import com.amit.converse.chat.dto.CreateChatRequest;
import com.amit.converse.chat.model.ChatRooms.SelfChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.DirectChatUserService;
import com.amit.converse.chat.service.User.SelfChatUserService;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class CreateSelfChatService {

    private final SelfChatService selfChatService;

    public static SelfChat getSelfChat(String name, String userId) {
        SelfChat selfChat = new SelfChat(name,new ArrayList<>(Arrays.asList(userId)));
        return selfChat;
    }

    public String create(CreateChatRequest chatRequest) throws InterruptedException {
        User primaryUser = UserService.getUserContext();
        selfChatService.processCreation(primaryUser,chatRequest);
        return selfChatService.getContextChatRoom().getId();
    }
}
