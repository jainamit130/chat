package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.exceptions.ConverseChatRoomNotFoundException;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.repository.ChatRoom.IChatRoomRepository;
import com.amit.converse.chat.repository.ChatRoom.IGroupRepository;
import com.amit.converse.chat.service.ClearChatService;
import com.amit.converse.chat.service.DeleteChatService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService extends ChatService<ChatContext<ITransactable>> {
    private final IGroupRepository groupRepository;
    private final CreateGroupChatService createGroupChatService;

    public GroupService(ChatContext<ITransactable> context, IChatRoomRepository chatRoomRepository, ClearChatService clearChatService, DeleteChatService deleteChatService, IGroupRepository groupRepository, CreateGroupChatService createGroupChatService) {
        super(context, chatRoomRepository, clearChatService, deleteChatService);
        this.groupRepository = groupRepository;
        this.createGroupChatService = createGroupChatService;
    }

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

    public String createChat(List<String> userIds) {
        GroupChat savedGroupChat = saveGroupChatToDB(createGroupChatService.create(userIds));
        updateChatRoomContext(savedGroupChat);
        return savedGroupChat.getId();
    }
}
