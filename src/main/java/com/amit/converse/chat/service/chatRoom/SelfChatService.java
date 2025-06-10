package com.amit.converse.chat.service.chatRoom;

import com.amit.converse.chat.dto.CreateChatRequest;
import com.amit.converse.chat.model.ChatRooms.SelfChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatRoom.ISelfChatRepository;
import com.amit.converse.chat.service.MessageService.DirectChatMessageService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SelfChatService extends ChatService<SelfChat>{
    @Autowired
    private ISelfChatRepository selfChatRepository;
    @Autowired
    private DirectChatMessageService directChatMessageService;

    public SelfChat saveSelfChatToDB(SelfChat selfChat) {
        SelfChat savedSelfChat = selfChatRepository.save(selfChat);
        updateChatRoomContext((SelfChat) fulfillChatRoom(savedSelfChat, UserService.getUserContext()));
        return savedSelfChat;
    }


    public SelfChat getChat(User user) {
        Optional<SelfChat> optionalSelfChat = selfChatRepository.findSelfChat(user.getUserId());
        if(optionalSelfChat.isPresent()) {
            SelfChat selfChat = optionalSelfChat.get();
            fulfillChatRoom(selfChat,user);
            updateChatRoomContext(selfChat);
            return selfChat;
        }
        return saveSelfChatToDB(CreateSelfChatService.getSelfChat(user.getDisplayName(),user.getUserId()));
    }

    public void processCreation(User primaryUser, CreateChatRequest directChatRequest) throws InterruptedException {
        // Updates the context, fulfilling its purpose of getting a chat new or existing
        getChat(primaryUser);
        directChatMessageService.sendMessage(directChatRequest.getMessage());
        return;
    }
}
