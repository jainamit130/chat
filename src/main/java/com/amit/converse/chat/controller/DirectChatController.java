package com.amit.converse.chat.controller;

import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.ChatRoom.DirectChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/converse/chat/direct/")
public class DirectChatController {

    private final DirectChatService directChatService;

    @PostMapping("/send/message/{chatRoomId}")
    public ResponseEntity sendMessage(@RequestParam String chatRoomId, @RequestBody ChatMessage message) {
        try {
            directChatService.sendMessage(message);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
