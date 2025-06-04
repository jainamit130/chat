package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.CreateChatRequest;
import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.service.chatRoom.CreateDirectChatService;
import com.amit.converse.chat.service.chatRoom.CreateGroupChatService;
import com.amit.converse.chat.service.chatRoom.CreateSelfChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/converse/chat/")
public class CreateChatController {
    private final CreateDirectChatService createDirectChatService;
    private final CreateSelfChatService createSelfChatService;
    private final CreateGroupChatService createGroupChatService;

    @PostMapping("create/self")
    public ResponseEntity<String> createSelfChat(@RequestBody CreateChatRequest chatRequest) {
        try {
            return new ResponseEntity(createSelfChatService.create(chatRequest), HttpStatus.OK);
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("create/direct/{userId}")
    public ResponseEntity<String> createDirectChat(@PathVariable String userId, @RequestBody CreateChatRequest directChatRequest) {
        try {
            return new ResponseEntity(createDirectChatService.create(userId,directChatRequest), HttpStatus.OK);
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("create/group")
    public ResponseEntity<String> createGroupChat(@RequestBody CreateGroupRequest groupRequest) {
        return new ResponseEntity(createGroupChatService.create(groupRequest), HttpStatus.OK);
    }
}
