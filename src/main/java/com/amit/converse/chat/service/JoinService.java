package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.chatRoom.GroupChatService;
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
    private final ChatConnectService chatConnectService;

    public void join(List<String> userIds) {
        GroupChat groupChat = groupChatService.getContextChatRoom();
        List<User> usersWhoDeletedChat = groupChatUserService.getUsersFromRepo(new ArrayList<>(groupChat.getDeletedForUsers()));
        chatConnectService.connectChatFromUsers(usersWhoDeletedChat,groupChat);

        List<String> newUserIds = new ArrayList<>();
        for (String userId : userIds) {
            if (!groupChat.getUserIds().contains(userId)) {
                newUserIds.add(userId);
            }
        }
        List<User> usersToBeAdded = groupChatUserService.getUsersFromRepo(newUserIds);
        groupChatService.joinChatRoom(newUserIds,groupChat);
        chatConnectService.processChatConnectionsAndNotify(usersToBeAdded,groupChat);
    }

}
