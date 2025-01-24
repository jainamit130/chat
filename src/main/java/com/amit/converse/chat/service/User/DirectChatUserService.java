package com.amit.converse.chat.service.User;

import com.amit.converse.chat.dto.OnlineUsers.DirectChatOnlineUsersDTO;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.ChatRoom.DirectChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DirectChatUserService extends UserChatService<DirectChat> {

    @Autowired
    private DirectChatService directChatService;

    public void processCreation() {
        DirectChat directChat = chatContext.getChatRoom();
        Set<String> deletedForUserIds = directChat.getDeletedForUsers();
        List<User> deletedForUsers = getUsersFromRepo(new ArrayList<>(deletedForUserIds));
        for(User user: deletedForUsers) {
            deletedForUsers.remove(user.getUserId());
            connectChat(user);
        }
        processUsersToDB(deletedForUsers);
        processChatRoomToDB(directChat);
    }

    public DirectChatOnlineUsersDTO getOnlineUsersDTO(List<String> onlineUserIds) {
        DirectChatOnlineUsersDTO.DirectChatOnlineUsersDTOBuilder directChatOnlineUsersDTOBuilder = DirectChatOnlineUsersDTO.builder();
        List<User> onlineUsers = getUsersFromRepo(onlineUserIds);
        Optional<User> optionalCounterPartUser = onlineUsers.stream()
                .filter(user -> !user.getUserId().equals(userContext.getUserId()))
                .findFirst();
        if(optionalCounterPartUser.isPresent()) {
            User counterPartUser = optionalCounterPartUser.get();
            directChatOnlineUsersDTOBuilder.lastSeenTimestamp(counterPartUser.getLastSeenTimestamp());
        }
        return directChatOnlineUsersDTOBuilder.build();
    }

    public String getCommonChatId(String userId) {
        Optional<DirectChat> directChat = directChatService.getCommonChat(userContext.getUser().getUserId(),userId);
        if(directChat.isPresent()) {
            return directChat.get().getId();
        }
        return null;
    }
}

/*
* its a new directChat => both users are connected
* or
* its a deleted directChat => only deleted users are connected
*
*
* */
