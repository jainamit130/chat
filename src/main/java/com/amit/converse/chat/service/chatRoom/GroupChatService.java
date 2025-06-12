package com.amit.converse.chat.service.chatRoom;

import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.exceptions.ConverseChatRoomNotFoundException;
import com.amit.converse.chat.model.ChatRooms.BlindPeriod;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatRoom.IGroupChatRepository;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupChatService extends ChatService<GroupChat> {
    @Autowired
    private IGroupChatRepository groupRepository;

    @Override
    public GroupChat getChatRoomById(String chatRoomId) {
        return groupRepository.findById(chatRoomId)
                .orElseThrow(() -> new ConverseChatRoomNotFoundException(chatRoomId));
    }

    public void joinChatRoom(List<String> userIds, GroupChat groupChat) {
        groupChat.join(userIds);
    }

    public void exitChatRoom(List<String> userIds,GroupChat groupChat) {
        groupChat.exit(userIds);
    }

    @Override
    public List<Message> getMessagesOfChatRoom() {
        List<Message> rawMessages = super.getMessagesOfChatRoom();
        GroupChat chatRoom = getContextChatRoom();
        User user = UserService.getUserContext();
        List<BlindPeriod> blindPeriods =
                chatRoom.getBlindPeriods().getOrDefault(user.getUserId(), List.of());

        return rawMessages.stream()
                .filter(msg -> blindPeriods.stream().noneMatch(
                        period -> !msg.getTimestamp().isBefore(period.getStart()) &&
                                !msg.getTimestamp().isAfter(period.getEnd())
                ))
                .toList();
    }


    @Override
    protected GroupChat saveChat(GroupChat groupChat) {
        return groupRepository.save(groupChat);
    }

    public void processCreation(CreateGroupRequest createGroupRequest,String adminUserId) {
        processChatRoomToDB(CreateGroupChatService.getGroupChat(createGroupRequest.getGroupName(), adminUserId));
    }

    public List<GroupChat> getCommonChats(String user1Id, String user2Id) {
        return groupRepository.findGroupChats(user1Id, user2Id).orElse(new ArrayList<GroupChat>());
    }
}
