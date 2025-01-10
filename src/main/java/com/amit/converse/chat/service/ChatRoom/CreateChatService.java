package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.CreateDirectChatRequest;
import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateChatService {

    private final ChatContext chatContext;
    private final UserContext userContext;
    private final DirectChatService directChatService;
    private final UserService userService;
    private final GroupService groupService;

    /*
     * Create Chat Service
     *
     * Group Chat -:
     * list should not be empty
     * Always create new group
     *
     * Direct Chat -:
     * should have a list of exactly 2 users
     * Check if already exists - if exists then don't create use that
     *
     * */

    /*
    *
    * Send Message
    *
    * Check if user is Exited or not if exited then return => any one can do
    * if not then proceed
    * save message in repository => chatService
    * send the message to chatRoom ||  process the message and re-save it
    * then for every user which are in deleted for users field will be sent newGroup Notification
    *
    *
    * Create Group Chat
    *
    * Create a new Group
    * Send users new Group Notification
    *
    * */

    public String createDirectChat(CreateDirectChatRequest directChatRequest) throws InterruptedException {
        directChatRequest.setPrimaryUserId(userContext.getUser().getUserId());
        DirectChat directChat = directChatService.createChat(directChatRequest);
        chatContext.setChatRoom(directChat);
        directChatService.sendMessage(directChatRequest.getMessage());
        directChatService.notifyChatToUser(directChatRequest.getUserId(),directChat);
        userService.joinChatRoom();
        return directChat.getId();
    }

    public String createGroupChat(CreateGroupRequest groupRequest) {
        GroupChat groupChat = groupService.createChat(groupRequest);
        groupService.notifyChatToUsers(groupChat);
        return groupChat.getId();
    }
}
