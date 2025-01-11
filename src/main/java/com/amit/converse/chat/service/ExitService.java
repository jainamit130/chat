package com.amit.converse.chat.service;

import com.amit.converse.chat.service.ChatRoom.GroupChatService;
import com.amit.converse.chat.service.Notification.NotifyGroupExitService;
import com.amit.converse.chat.service.User.GroupChatUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExitService {
    private final GroupChatService groupChatService;
    private final GroupChatUserService groupChatUserService;
    private final NotifyGroupExitService exitNotificationService;

    public void leave(List<String> userIds) {
        groupChatService.leaveChatRoom(userIds);
        exitNotificationService.notifyGroup(groupChatUserService.getUsersFromRepo(userIds));
        groupChatUserService.exitChatRoom();
    }
}
