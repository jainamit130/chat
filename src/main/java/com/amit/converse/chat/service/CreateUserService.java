package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.UserDTO;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.SelfChatUserService;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateUserService {

    @Autowired
    private SelfChatUserService selfChatUserService;

    public static User getUser(UserDTO userDTO) {
        return User.builder()
               .userId(userDTO.getUserId())
               .username(userDTO.getUsername())
               .creationDate(userDTO.getCreationDate())
               .lastSeenTimestamp(userDTO.getCreationDate())
               .build();
    }


    public void createUser(UserDTO userDTO) {
        getUser(userDTO);
        selfChatUserService.getSelfChatId();
    }

}
