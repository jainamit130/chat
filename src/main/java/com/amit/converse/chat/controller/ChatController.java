package com.amit.converse.chat.controller;

import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.ClearChatService;
import com.amit.converse.chat.service.DeleteChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ClearChatService clearChatService;
    private final DeleteChatService deleteChatService;

    @PostMapping("/chat/clearChat/{chatRoomId}")
    public ResponseEntity clearChat() {
        try {
            clearChatService.clearChat();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/chat/deleteChat/{chatRoomId}")
    public ResponseEntity<Boolean> deleteChat() {
        try {
            deleteChatService.deleteChat();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
