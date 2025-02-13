package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.SelfChat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class CreateSelfChatService {

    public static SelfChat getSelfChat(String name, String userId) {
        SelfChat selfChat = new SelfChat(name,List.of(userId));
        return selfChat;
    }
}
