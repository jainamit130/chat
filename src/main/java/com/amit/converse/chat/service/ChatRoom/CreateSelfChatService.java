package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.model.ChatRooms.SelfChat;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class CreateSelfChatService {

    public static SelfChat getSelfChat(String name, String userId) {
        SelfChat selfChat = SelfChat.builder().build();
        selfChat.setChatRoomType(ChatRoomType.SELF);
        selfChat.setName(name);
        selfChat.setUserIds(List.of(userId));
        selfChat.setCreatedAt(Instant.now());
        return selfChat;
    }
}
