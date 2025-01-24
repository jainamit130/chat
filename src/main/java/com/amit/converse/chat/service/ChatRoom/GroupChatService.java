package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.exceptions.ConverseChatRoomNotFoundException;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.repository.ChatRoom.IGroupChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupChatService extends ChatService<GroupChat> {
    @Autowired
    private IGroupChatRepository groupRepository;
    @Autowired
    private GroupMessageService messageService;

    @Override
    public GroupChat getChatRoomById(String chatRoomId) {
        return groupRepository.findById(chatRoomId)
                .orElseThrow(() -> new ConverseChatRoomNotFoundException(chatRoomId));
    }

    public void joinChatRoom(List<String> userIds) {
        GroupChat groupChat = context.getChatRoom();
        groupChat.join(userIds);
        processChatRoomToDB(groupChat);
    }

    public void exitChatRoom(List<String> userIds) {
        GroupChat groupChat = context.getChatRoom();
        groupChat.exit(userIds);
        processChatRoomToDB(groupChat);
    }

    @Override
    protected GroupChat saveChat(GroupChat groupChat) {
        return groupRepository.save(groupChat);
    }

    public void processCreation(CreateGroupRequest createGroupRequest) {
        processChatRoomToDB(CreateGroupChatService.getGroupChat(createGroupRequest.getGroupName(),createGroupRequest.getUserIds()));
    }

    public List<GroupChat> getCommonChats(String user1Id, String user2Id) {
        return groupRepository.findGroupChats(user1Id, user2Id).orElse(new ArrayList<GroupChat>());
    }
}
