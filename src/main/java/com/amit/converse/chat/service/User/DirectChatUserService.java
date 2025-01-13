package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class DirectChatUserService extends UserChatService {

    public void processCreation() {
        IChatRoom directChat = chatContext.getChatRoom();
        Set<String> deletedForUserIds = directChat.getDeletedForUsers();
        List<User> deletedForUsers = getUsersFromRepo(new ArrayList<>(deletedForUserIds));
        for(User user: deletedForUsers) {
            deletedForUsers.remove(user.getUserId());
            connectChat(user);
        }
        processUsersToDB(deletedForUsers);
        processChatRoomToDB(directChat);
    }
}

/*
* its a new directChat => both users are connected
* or
* its a deleted directChat => only deleted users are connected
*
*
* */
