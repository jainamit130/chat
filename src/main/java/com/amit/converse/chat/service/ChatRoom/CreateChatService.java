package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.CreateGroupRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateChatService {

    private final UserContext userContext;
    private final DirectChatService directChatService;
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

    public String createDirectChat(String userId) {

    }

    public String createGroupChat(CreateGroupRequest groupRequest) {
    }
}
