package com.amit.converse.chat.service;

import com.amit.converse.chat.service.ChatRoom.GroupService;
import com.amit.converse.chat.service.Notification.NotifyGroupJoinService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class JoinService {
    private final GroupService groupService;
    private final UserService userService;
    private final NotifyGroupJoinService joinNotificationService;

    public void join(List<String> userIds) {
        groupService.joinChatRoom(userIds);
        userService.joinChatRoom();
        joinNotificationService.notifyGroup(userService.getUsersFromRepo(userIds));
    }
}
