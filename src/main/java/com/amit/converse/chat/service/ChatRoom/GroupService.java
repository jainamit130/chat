package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.GroupContext;
import com.amit.converse.chat.exceptions.ConverseChatRoomNotFoundException;
import com.amit.converse.chat.repository.ChatRoom.IGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupService {
    private final ChatContext<ITransactable> context;
    private final IGroupRepository groupRepository;

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
}
