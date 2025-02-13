package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.dto.CreateDirectChatRequest;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatRoom.IDirectChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DirectChatService extends ChatService<DirectChat> {
    @Autowired
    private IDirectChatRepository directChatRepository;

    private void processDirectChatCreation(User primaryUser, User counterPartUser) {
        Optional<DirectChat> alreadyExistingDirectChat = getChatIfAlreadyExisting(primaryUser.getUserId(),counterPartUser.getUserId());
        if(alreadyExistingDirectChat.isPresent()) {
            updateChatRoomContext(alreadyExistingDirectChat.get());
        } else {
            processChatRoomToDB(CreateDirectChatService.getNewDirectChat(primaryUser, counterPartUser));
        }
    }

    private Optional<DirectChat> getChatIfAlreadyExisting(String primaryUserId, String counterPartUserId) {
        return directChatRepository.findDirectChat(primaryUserId,counterPartUserId);
    }

    @Override
    protected DirectChat saveChat(DirectChat directChat) {
        return directChatRepository.save(directChat);
    }

    public void processCreation(User primaryUser, User counterPartUser,CreateDirectChatRequest directChatRequest) throws InterruptedException {
        // Updates the context, fulfilling its purpose of getting a chat new or existing
        processDirectChatCreation(primaryUser,counterPartUser);
        sendMessage(directChatRequest.getMessage());
        return;
    }

    public Optional<DirectChat> getCommonChat(String user1Id, String user2Id) {
        return directChatRepository.findDirectChat(user1Id,user2Id);
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
