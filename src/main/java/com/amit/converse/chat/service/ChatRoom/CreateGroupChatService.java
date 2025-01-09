package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import com.amit.converse.chat.repository.ChatRoom.IDirectChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
@AllArgsConstructor
public class CreateGroupChatService {

    public GroupChat create(List<String> userIds) {
        GroupChat groupChat = GroupChat.builder().build();
        groupChat.setUserIds(userIds);
        groupChat.setChatRoomType(ChatRoomType.GROUP);
        return groupChat;
    }
}
