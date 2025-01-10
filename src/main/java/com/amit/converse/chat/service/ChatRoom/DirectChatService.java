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
        return directChatRepository.save(directChat);
    }

    public DirectChat createChat(CreateDirectChatRequest directChatRequest) {
        String primaryUserId = directChatRequest.getPrimaryUserId();
        String userId = directChatRequest.getUserId();
        Optional<DirectChat> directChat = directChatRepository.findDirectChat(primaryUserId,userId);
        if(directChat.isPresent()) {
            directChat.get();
        }
        DirectChat savedDirectChat = saveDirectChatToDB(createDirectChatService.create(primaryUserId,userId));
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
