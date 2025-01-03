package com.amit.converse.chat.controller;

import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatContext chatContext;
    private final ChatService chatService;
    private final UserService userService;

    @ModelAttribute
    public void populateChatContext(@RequestParam String userId, @RequestParam String chatRoomId) {
        chatContext.setChatRoom(chatService.getChatRoomById(chatRoomId));
        chatContext.setUser(userService.getLoggedInUser());
    }

    @PostMapping("/groups/clearChat/{chatRoomId}")
    public ResponseEntity<> clearChat(@ModelAttribute ChatContext chatContext) {
        try {
            return new ResponseEntity(chatService.clearChat(););
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/deleteChat/{chatRoomId}")
    public ResponseEntity<Boolean> deleteChat(@ModelAttribute ChatContext chatContext) {
        try {
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/joinChat/{chatRoomId}")
    public ResponseEntity<Boolean> joinChat(@ModelAttribute ChatContext chatContext) {
        try {
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/exitChat/{chatRoomId}")
    public ResponseEntity<Boolean> exitChat(@ModelAttribute ChatContext chatContext) {
        try {
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}
