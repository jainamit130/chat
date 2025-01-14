package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.dto.OnlineUsers.DirectChatOnlineUsersDTO;
import com.amit.converse.chat.dto.OnlineUsers.GroupChatOnlineUsersDto;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DirectChatUserService extends UserChatService<DirectChat> {

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
}

/*
* its a new directChat => both users are connected
* or
* its a deleted directChat => only deleted users are connected
*
*
* */
