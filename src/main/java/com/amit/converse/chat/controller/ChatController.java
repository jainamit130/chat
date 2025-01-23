package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.GroupDetails;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chat/clearChat/{chatRoomId}")
    public ResponseEntity clearChat(@RequestParam String chatRoomId) {
        try {
            chatService.clearChat();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/chat/deleteChat/{chatRoomId}")
    public ResponseEntity<Boolean> deleteChat(@RequestParam String chatRoomId) {
        try {
            chatService.deleteChat();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
