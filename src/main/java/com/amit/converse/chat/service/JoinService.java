package com.amit.converse.chat.service;

import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.MessageService.MessageMemberCountProcessingService;
import com.amit.converse.chat.service.User.UserService;
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
    private final ChatMessageService chatMessageService;

    public void join(List<String> userIds, boolean shareHistory) throws ConverseException {
        GroupChat groupChat = groupChatService.getContextChatRoom();
        if(!groupChat.getIsNewlyFormed() && !groupChat.isPartOfGroup(UserService.getUserContext().getUserId())) throw new ConverseException("Unable to join group");
        List<User> usersWhoDeletedChat = groupChatUserService.getUsersFromRepo(new ArrayList<>(groupChat.getDeletedForUsers()));
        chatConnectService.connectChatFromUsers(usersWhoDeletedChat,groupChat);

        List<String> newUserIds = new ArrayList<>();
        for (String userId : userIds) {
            if (!groupChat.getUserIds().contains(userId)) {
                newUserIds.add(userId);
            }
        }
        groupChatService.joinChatRoom(new ArrayList<>(newUserIds),groupChat,shareHistory);
        List<User> usersToBeAdded = groupChatUserService.getUsersFromRepo(newUserIds);
        chatConnectService.processChatConnectionsAndNotify(usersToBeAdded,groupChat,chatMessageService.processMemberCountOfMessages(groupChat,usersToBeAdded),shareHistory);
    }

}
