package com.amit.converse.chat.service.User;

import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.dto.OnlineUsers.DirectChatOnlineUsersDTO;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.chatRoom.DirectChatService;
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
        DirectChat directChat = chatService.getContextChatRoom();
        Set<String> deletedForUserIds = directChat.getDeletedForUsers();
        List<User> deletedForUsers = getUsersFromRepo(new ArrayList<>(deletedForUserIds));
        for(User user: deletedForUsers) {
            deletedForUsers.remove(user.getUserId());
            connectChatAndNotify(user,directChat);
        }
        processUsersToDB(deletedForUsers);
        processChatRoomToDB(directChat);
    }

    public User getCounterPartUser(List<String> userIds) {
        if(userIds.size()!=2) throw new ConverseException("Invalid Chat!");
        String counterPartUserId = userIds.stream().filter(userId -> !userId.equals(UserContext.getUser())).findFirst().get();
        return getUserFromRepo(counterPartUserId);
    }

    @Override
    public DirectChatOnlineUsersDTO getOnlineUsersDTO(List<String> onlineUserIds) {
        DirectChatOnlineUsersDTO.DirectChatOnlineUsersDTOBuilder directChatOnlineUsersDTOBuilder = DirectChatOnlineUsersDTO.builder();
        List<User> onlineUsers = getUsersFromRepo(onlineUserIds);
        Optional<User> optionalCounterPartUser = onlineUsers.stream()
                .filter(user -> !user.getUserId().equals(UserContext.getUser()))
                .findFirst();
        if(optionalCounterPartUser.isPresent()) {
            User counterPartUser = optionalCounterPartUser.get();
            directChatOnlineUsersDTOBuilder.lastSeenTimestamp(counterPartUser.getLastSeenTimestamp());
        }
        return directChatOnlineUsersDTOBuilder.build();
    }

    public String getCommonChatId(String userId) {
        Optional<DirectChat> directChat = directChatService.getCommonChat(UserContext.getUserId(),userId);
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
