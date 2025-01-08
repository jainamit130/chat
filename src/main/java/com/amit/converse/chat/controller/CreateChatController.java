package com.amit.converse.chat.controller;

import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.service.ChatRoom.CreateChatService;
import com.amit.converse.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CreateChatController {

    private final UserContext userContext;
    private final UserService userService;
    private final CreateChatService createChatService;

    @ModelAttribute
    public void populateUserContext() {
        userContext.setUser(userService.getLoggedInUser());
    }

    @PostMapping("create/chat/direct/{userId}")
    public ResponseEntity<String> createDirectChat(@RequestParam String userId) {
        return new ResponseEntity(createChatService.createDirectChat(userId), HttpStatus.OK);
    }

    @PostMapping("create/chat/group")
    public ResponseEntity<String> createDirectChat(@RequestBody CreateGroupRequest groupRequest) {
        return new ResponseEntity(createChatService.createGroupChat(groupRequest), HttpStatus.OK);
    }
}
