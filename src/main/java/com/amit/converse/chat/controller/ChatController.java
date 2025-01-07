package com.amit.converse.chat.controller;

import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.JoinService;
import com.amit.converse.chat.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatContext chatContext;
    private final UserContext userContext;
    private final ChatService chatService;
    private final UserService userService;

    @ModelAttribute
    public void populateChatContext(@RequestParam String chatRoomId) {
        chatContext.setChatRoom(chatService.getChatRoomById(chatRoomId));
        userContext.setUser(userService.getLoggedInUser());
    }

    @PostMapping("/groups/clearChat/{chatRoomId}")
    public ResponseEntity clearChat(@RequestParam String chatRoomId) {
        try {
            chatService.clearChat();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/deleteChat/{chatRoomId}")
    public ResponseEntity<Boolean> deleteChat(@RequestParam String chatRoomId) {
        try {
            chatService.deleteChat();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
