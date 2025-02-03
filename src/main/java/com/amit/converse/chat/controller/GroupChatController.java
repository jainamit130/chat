package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.GroupDetails;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.ChatRoom.GroupChatService;
import com.amit.converse.chat.service.ExitService;
import com.amit.converse.chat.service.JoinService;
import com.amit.converse.chat.service.User.GroupChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/converse/chat/group/")
public class GroupChatController {
    private final GroupChatService groupChatService;
    private final GroupChatUserService groupChatUserService;
    private final JoinService joinService;
    private final ExitService exitService;

    @PostMapping("/send/message/{chatRoomId}")
    public ResponseEntity sendMessage(@RequestBody ChatMessage chatMessage) {
        try{
            groupChatService.sendMessage(chatMessage);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/add/users/{chatRoomId}")
    public ResponseEntity joinChat(@RequestBody List<String> userIds) {
        try {
            joinService.join(userIds);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/remove/users/{chatRoomId}")
    public ResponseEntity<Boolean> exitChat(@RequestBody List<String> userIds) {
        try {
            exitService.leave(userIds);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/get/details/{chatRoomId}")
    public ResponseEntity<GroupDetails> getGroupDetails() {
        try {
            return new ResponseEntity(groupChatUserService.getGroupDetails(),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

/*
*
* create direct chat
* create group chat
* send message
* add member
* remove member
* exit group
* get Group Details
* get message details
* det user details
* clear chat
* delete chat
* delete message
*
*
*
* */
