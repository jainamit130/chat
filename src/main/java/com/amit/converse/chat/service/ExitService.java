package com.amit.converse.chat.service;

import com.amit.converse.chat.service.ChatRoom.GroupService;
import com.amit.converse.chat.service.Notification.NotifyGroupExitService;
import com.amit.converse.chat.service.Notification.NotifyGroupJoinService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExitService {
    private final GroupService groupService;
    private final UserService userService;
    private final NotifyGroupExitService exitNotificationService;

    public void leave(List<String> userIds) {
        groupService.leaveChatRoom(userIds);
        userService.exitChatRoom();
        exitNotificationService.notifyGroup(userService.getUsersFromRepo(userIds));
    }
}
