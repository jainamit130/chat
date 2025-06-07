package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.UserService;
import com.amit.converse.chat.service.chatRoom.GroupChatService;
import com.amit.converse.chat.service.User.GroupChatUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ExitService {
    private final GroupChatService groupChatService;
    private final GroupChatUserService groupChatUserService;
    private final ChatDisconnectService chatDisconnectService;

    // LoggedIn User exits
    public void leave() {
        leave(Collections.singletonList(UserService.getUserContext().getUserId()));
    }

    // Users removed from group
    public void leave(List<String> userIds) {
        GroupChat groupChat = groupChatService.getContextChatRoom();
        List<User> users = groupChatUserService.getUsersFromRepo(userIds);
        chatDisconnectService.connectChatFromUserIds(new ArrayList<>(groupChat.getDeletedForUsers()),groupChat);
        groupChatService.exitChatRoom(userIds);
        chatDisconnectService.processChatConnectionsAndNotify(users,groupChat);
    }
}
