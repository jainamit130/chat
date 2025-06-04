package com.amit.converse.chat.service;

import com.amit.converse.chat.State.Offline;
import com.amit.converse.chat.State.Online;
import com.amit.converse.chat.State.StateFactoryService;
import com.amit.converse.chat.dto.UserDTO;
import com.amit.converse.chat.model.ChatRooms.SelfChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.SelfChatUserService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CreateUserService {

    @Autowired
    private UserChatService userChatService;

    @Autowired
    private SelfChatUserService selfChatUserService;

    @Autowired
    private StateFactoryService stateFactoryService;

    private User getUser(UserDTO userDTO) {
        User user = User.builder()
               .userId(userDTO.getUserId())
               .username(userDTO.getUsername())
               .creationDate(userDTO.getCreationDate())
               .lastSeenTimestamp(userDTO.getCreationDate())
               .build();
        return user;
    }


    public void createUser(UserDTO userDTO) {
        User createdUser = userChatService.createUser(getUser(userDTO));
        SelfChat selfChat = selfChatUserService.getSelfChat();
        userChatService.connectChat(createdUser, selfChat);
        userChatService.processUsersAndChatRoomToDB(Collections.singletonList(createdUser),selfChat);
    }

}
