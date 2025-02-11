package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.UserDTO;
import com.amit.converse.chat.model.ChatRooms.SelfChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.SelfChatUserService;
import com.amit.converse.chat.service.User.UserChatService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    @Autowired
    private UserChatService userChatService;

    @Autowired
    private SelfChatUserService selfChatUserService;

    private User getUser(UserDTO userDTO) {
        return User.builder()
               .userId(userDTO.getUserId())
               .username(userDTO.getUsername())
               .creationDate(userDTO.getCreationDate())
               .lastSeenTimestamp(userDTO.getCreationDate())
               .build();
    }


    public void createUser(UserDTO userDTO) {
        User createdUser = userChatService.createUser(getUser(userDTO));
        userChatService.connectChat(createdUser, selfChatUserService.getSelfChat());
    }

}
