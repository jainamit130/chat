package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.dto.CreateGroupRequest;
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
    private GroupMessageService messageService;

    public ITransactable getGroupById(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ConverseChatRoomNotFoundException(groupId));
    }

    public void joinChatRoom(List<String> userIds) {
        ITransactable transactableRoom = context.getChatRoom();
        transactableRoom.join(userIds);
        processChatRoomToDB(transactableRoom);
    }

    public void exitChatRoom(List<String> userIds) {
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
        GroupChat savedGroupChat = groupRepository.save(groupChat);
        updateChatRoomContext(savedGroupChat);
        return savedGroupChat;
    }

    public void sendMessage(ChatMessage chatMessage) throws InterruptedException {
        messageService.sendMessage(chatMessage);
    }

    public void processCreation(CreateGroupRequest createGroupRequest) {
        saveGroupChatToDB(CreateGroupChatService.getGroupChat(createGroupRequest.getGroupName(),createGroupRequest.getUserIds()));
    }

}
