package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class CreateGroupChatService {

    public GroupChat create(String name, List<String> userIds) {
        GroupChat groupChat = GroupChat.builder().build();
        groupChat.setName(name);
        groupChat.setUserIds(userIds);
        groupChat.setChatRoomType(ChatRoomType.GROUP);
        return groupChat;
    }
}
