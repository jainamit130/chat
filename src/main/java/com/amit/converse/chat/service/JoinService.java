package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.chatRoom.GroupChatService;
import com.amit.converse.chat.service.Notification.NotifyGroupJoinService;
import com.amit.converse.chat.service.User.GroupChatUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class JoinService {
    private final GroupChatService groupChatService;
    private final GroupChatUserService groupChatUserService;
    private final NotifyGroupJoinService joinNotificationService;

    public void join(List<String> userIds) {
        GroupChat groupChat = groupChatService.getContextChatRoom();
        List<User> users = groupChatUserService.getUsersFromRepo(userIds);
        groupChatService.joinChatRoom(userIds,groupChat);
        groupChatUserService.connectChat(users,groupChat);
        joinNotificationService.notifyGroup(users);
        groupChatUserService.connectChatFromUserIds(new ArrayList<>(groupChat.getDeletedForUsers()),groupChat);
    }

}
