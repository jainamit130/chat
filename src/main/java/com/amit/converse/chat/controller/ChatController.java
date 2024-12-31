package com.amit.converse.chat.controller;

import com.amit.converse.chat.context.ChatContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatContext chatContext;

    @ModelAttribute
    

    @PostMapping("/groups/clearChat/{chatRoomId}")
    public ResponseEntity<Boolean> clearChat(@ModelAttribute ChatContext chatContext) {
        try {
            return new ResponseEntity(groupService.clearChat(chatRoomId,userId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
