package com.amit.converse.chat.service.User;

import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.dto.OnlineUsers.DirectChatOnlineUsersDTO;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.chatRoom.DirectChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DirectChatUserService extends UserChatService<DirectChat> {

    @Autowired
    private DirectChatService directChatService;

    public void processCreation() {
        DirectChat directChat = chatService.getContextChatRoom();

        Set<String> deletedForUserIds = new HashSet<>(directChat.getDeletedForUsers());

        List<User> deletedUsers = getUsersFromRepo(new ArrayList<>(deletedForUserIds));

        for (User user : deletedUsers) {
            directChat.getDeletedForUsers().remove(user.getUserId());
            connectChatAndNotify(user, directChat);
        }

        processUsersToDB(deletedUsers);
        processChatRoomToDB(directChat);
    }


    public User getCounterPartUser(List<String> userIds) {
        if(userIds.size()!=2) throw new ConverseException("Invalid Chat!");
        String counterPartUserId = userIds.stream().filter(userId -> !userId.equals(UserService.getUserContext().getUserId())).findFirst().get();
        return getUserFromRepo(counterPartUserId);
    }

    @Override
    public DirectChatOnlineUsersDTO getOnlineUsersDTO(List<String> onlineUserIds) {
        DirectChatOnlineUsersDTO.DirectChatOnlineUsersDTOBuilder directChatOnlineUsersDTOBuilder = DirectChatOnlineUsersDTO.builder();
        List<User> onlineUsers = getUsersFromRepo(onlineUserIds);
        Optional<User> optionalCounterPartUser = onlineUsers.stream()
                .filter(user -> !user.getUserId().equals(UserService.getUserContext().getUserId()))
                .findFirst();
        if(!optionalCounterPartUser.isPresent()) {
            User counterPartUser = getCounterPartUser(ChatContext.getChatRoom().getAllUserIds());
            directChatOnlineUsersDTOBuilder.lastSeenTimestamp(counterPartUser.getLastSeenTimestamp());
        }
        return directChatOnlineUsersDTOBuilder.build();
    }

    public String getCommonChatId(String userId) {
        Optional<DirectChat> directChat = directChatService.getCommonChat(UserService.getUserContext().getUserId(),userId);
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
