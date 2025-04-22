package com.amit.converse.chat.service.chatRoom;

import com.amit.converse.chat.model.ChatRooms.SelfChat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CreateSelfChatService {

    public static SelfChat getSelfChat(String name, String userId) {
        SelfChat selfChat = new SelfChat(name,new ArrayList<>(List.of(userId)));
        return selfChat;
    }
}
