package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.exceptions.ConverseChatRoomNotFoundException;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.repository.ChatRoom.IGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupChatService extends ChatService<GroupChat> {
    @Autowired
    private IGroupRepository groupRepository;
    @Autowired
    private CreateGroupChatService createGroupChatService;


    public ITransactable getGroupById(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ConverseChatRoomNotFoundException(groupId));
    }

    public void joinChatRoom(List<String> userIds) {
        ITransactable transactableRoom = context.getChatRoom();
        transactableRoom.join(userIds);
        processChatRoomToDB(transactableRoom);
    }

    public void leaveChatRoom(List<String> userIds) {
        ITransactable transactableRoom = context.getChatRoom();
        transactableRoom.exit(userIds);
        processChatRoomToDB(transactableRoom);
    }

    public void processChatRoomToDB(ITransactable transactableRoom) {
        if (transactableRoom.isDeletable()) {
            groupRepository.deleteById(transactableRoom.getId());
        } else {
            groupRepository.save(transactableRoom);
        }
    }

    public GroupChat saveGroupChatToDB(GroupChat groupChat) {
        return groupRepository.save(groupChat);
    }

    public void sendMessage(ChatMessage chatMessage) {
    }

    public GroupChat createChat(CreateGroupRequest createGroupRequest) {
        GroupChat savedGroupChat = saveGroupChatToDB(createGroupChatService.create(createGroupRequest.getGroupName(),createGroupRequest.getUserIds()));
        updateChatRoomContext(savedGroupChat);
        return savedGroupChat;
    }

    public void notifyChatToUsers(GroupChat groupChat) {
        NewChatNotification newChatNotification = NewChatNotification.builder().chatRoom(groupChat).build();
        for(String userId:groupChat.getUserIds()) {
            super.notifyChatToUser(userId,groupChat);
        }
    }
}
