package com.amit.converse.chat.service.chatRoom;

import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.service.User.GroupChatUserService;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class CreateGroupChatService {

    private final ChatContext chatContext;
    private final GroupChatService groupChatService;
    private final GroupChatUserService groupChatUserService;

    public static GroupChat getGroupChat(String name, List<String> userIds, String adminUserId) {
        GroupChat groupChat = new GroupChat(name,userIds, adminUserId);
        return groupChat;
    }

    public String create(CreateGroupRequest createGroupRequest) {
        createGroupRequest.addUserId(UserService.getUserContext().getUserId());
        groupChatService.processCreation(createGroupRequest,UserService.getUserContext().getUserId());
        groupChatUserService.processCreation();
        return chatContext.getChatRoom().getId();
    }
}
