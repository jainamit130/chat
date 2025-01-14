package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import com.amit.converse.chat.service.User.DirectChatUserService;
import com.amit.converse.chat.service.User.GroupChatUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@AllArgsConstructor
public class CreateGroupChatService {

    private final UserContext userContext;
    private final ChatContext chatContext;
    private final GroupChatService groupChatService;
    private final GroupChatUserService groupChatUserService;

    public static GroupChat getGroupChat(String name, List<String> userIds) {
        GroupChat groupChat = GroupChat.builder().build();
        groupChat.setName(name);
        groupChat.setUserIds(userIds);
        groupChat.setChatRoomType(ChatRoomType.GROUP);
        return groupChat;
    }

    public String create(CreateGroupRequest createGroupRequest) {
        createGroupRequest.addUserId(userContext.getUserId());
        groupChatService.processCreation(createGroupRequest);
        groupChatUserService.processCreation();
        return chatContext.getChatRoom().getId();
    }
}
