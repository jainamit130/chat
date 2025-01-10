package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
@AllArgsConstructor
public class CreateDirectChatService {

    public DirectChat create(String partUserId, String counterPartUserId) {
        DirectChat directChat = DirectChat.builder().build();
        directChat.setUserIds(Arrays.asList(partUserId,counterPartUserId));
        directChat.setChatRoomType(ChatRoomType.DIRECT);
        return directChat;
    }
}
