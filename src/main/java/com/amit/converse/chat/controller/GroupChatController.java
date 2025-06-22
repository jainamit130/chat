package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.GroupDetails;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.MessageService.GroupChatMessageService;
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
    private final GroupChatUserService groupChatUserService;
    private final JoinService joinService;
    private final ExitService exitService;

    @PostMapping("/add/users/{chatRoomId}")
    public ResponseEntity joinChat(@RequestBody List<String> userIds,@RequestParam(required = false,defaultValue = "false") boolean shareHistory) {
        try {
            joinService.join(userIds,shareHistory);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/exit/{chatRoomId}")
    public ResponseEntity exitChat() {
        exitService.leave();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/remove/users/{chatRoomId}")
    public ResponseEntity removeUsers(@RequestBody List<String> userIds) {
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
* create direct chat => done
* create group chat => done
* send message => done
* add member => done
* remove member => on going
* exit group => on going
* get Group Details => pending
* get message details => done
* det user details
* clear chat => done
* delete chat => done
* delete message =>done
*
*
*
* */
