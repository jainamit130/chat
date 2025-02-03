package com.amit.converse.chat.service;

import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.ChatRoom.GroupChatService;
import com.amit.converse.chat.service.MessageService.SaveChatMessageService;
import com.amit.converse.chat.service.Notification.NotifyGroupJoinService;
import com.amit.converse.chat.service.User.GroupChatUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class JoinService {
    private final GroupChatService groupChatService;
    private final GroupChatUserService groupChatUserService;
    private final NotifyGroupJoinService joinNotificationService;

    public void join(List<String> userIds) {
        List<User> users = groupChatUserService.getUsersFromRepo(userIds);
        groupChatService.joinChatRoom(userIds);
        groupChatUserService.join(users);
        joinNotificationService.notifyGroup(users);
    }
}
