package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.dto.CreateDirectChatRequest;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.repository.ChatRoom.IDirectChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DirectChatService extends ChatService<DirectChat> {
    @Autowired
    private IDirectChatRepository directChatRepository;
    @Autowired
    private CreateDirectChatService createDirectChatService;

    public DirectChat saveDirectChatToDB(DirectChat directChat) {
        DirectChat savedDirectChat = directChatRepository.save(directChat);
        updateChatRoomContext(savedDirectChat);
        return savedDirectChat;
    }

    Optional<DirectChat> getChatIfAlreadyExisting(String primaryUserId, String counterPartUserId) {
        return directChatRepository.findDirectChat(primaryUserId,counterPartUserId);
    }

    public DirectChat createChat(CreateDirectChatRequest directChatRequest) {
        String primaryUserId = directChatRequest.getPrimaryUserId();
        String counterPartUserId = directChatRequest.getUserId();
        Optional<DirectChat> alreadyExistingDirectChat = getChatIfAlreadyExisting(primaryUserId,counterPartUserId);
        if(alreadyExistingDirectChat.isPresent()) {
            return alreadyExistingDirectChat.get();
        }
        DirectChat savedDirectChat = saveDirectChatToDB(createDirectChatService.create(primaryUserId,counterPartUserId));
        updateChatRoomContext(savedDirectChat);
        return savedDirectChat;
    }

}


/*
*
* create chat
* send message
* notifyChat
*
* create group
* notifyGroup
*
* */
