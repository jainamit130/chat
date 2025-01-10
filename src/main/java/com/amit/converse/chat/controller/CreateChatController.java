package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.CreateDirectChatRequest;
import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.service.ChatRoom.CreateChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/converse/chat/")
public class CreateChatController {
    private final CreateChatService createChatService;

    @PostMapping("create/direct")
    public ResponseEntity<String> createDirectChat(@RequestBody CreateDirectChatRequest directChatRequest) {
        try {
            return new ResponseEntity(createChatService.createDirectChat(directChatRequest), HttpStatus.OK);
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("create/group")
    public ResponseEntity<String> createGroupChat(@RequestBody CreateGroupRequest groupRequest) {
        return new ResponseEntity(createChatService.createGroupChat(groupRequest), HttpStatus.OK);
    }
}
