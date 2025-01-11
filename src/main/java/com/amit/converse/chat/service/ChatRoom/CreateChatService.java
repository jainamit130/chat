package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.CreateDirectChatRequest;
import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateChatService {

    private final ChatContext chatContext;
    private final UserContext userContext;
    private final DirectChatService directChatService;
    private final UserService userService;
    private final GroupChatService groupChatService;

    public String createDirectChat(CreateDirectChatRequest directChatRequest) throws InterruptedException {
        directChatRequest.setPrimaryUserId(userContext.getUser().getUserId());
        DirectChat directChat = directChatService.createChat(directChatRequest);
        directChatService.sendMessage(directChatRequest.getMessage());
        directChatService.notifyChatToUser(directChatRequest.getUserId(),directChat);
        userService.joinChatRoom();
        return directChat.getId();
    }

    public String createGroupChat(CreateGroupRequest groupRequest) {
        GroupChat groupChat = groupChatService.createChat(groupRequest);
        groupChatService.notifyChatToUsers(groupChat);
        return groupChat.getId();
    }
}
