package com.amit.converse.chat.service;

import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.chatRoom.GroupChatService;
import com.amit.converse.chat.service.Notification.NotifyGroupExitService;
import com.amit.converse.chat.service.User.GroupChatUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ExitService {
    private final GroupChatService groupChatService;
    private final GroupChatUserService groupChatUserService;
    private final NotifyGroupExitService exitNotificationService;

    // LoggedIn User exits
    public void leave() {
        leave(Collections.singletonList(groupChatUserService.getContextUser().getUserId()));
    }

    // Users removed from group
    public void leave(List<String> userIds) {
        List<User> users = groupChatUserService.getUsersFromRepo(userIds);
        groupChatService.exitChatRoom(userIds);
        notifyAndExit(users);
    }

    // notify the exit to the group and exit
    private void notifyAndExit(List<User> users) {
        exitNotificationService.notifyGroup(users);
        groupChatUserService.exit(users);
    }
}
