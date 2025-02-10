package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.SelfChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatRoom.ISelfChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SelfChatService extends ChatService<SelfChat>{
    @Autowired
    private ISelfChatRepository selfChatRepository;

    public SelfChat saveSelfChatToDB(SelfChat selfChat) {
        SelfChat savedSelfChat = selfChatRepository.save(selfChat);
        updateChatRoomContext(savedSelfChat);
        return savedSelfChat;
    }

    public SelfChat getChat(User user) {
        Optional<SelfChat> selfChat = selfChatRepository.findSelfChat(user.getUserId());
        if(selfChat.isPresent()) {
            return selfChat.get();
        }
        return saveSelfChatToDB(CreateSelfChatService.getSelfChat(user.getUsername(),user.getUserId()));
    }
}
