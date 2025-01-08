package com.amit.converse.chat.controller;

import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.ChatRoom.DirectChatService;
import com.amit.converse.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DirectChatController {

    private final ChatContext chatContext;
    private final UserService userService;
    private final DirectChatService directChatService;

    @ModelAttribute
    public void populateChatContext(@RequestParam String chatRoomId) {
        chatContext.setChatRoom(directChatService.getChatRoomById(chatRoomId));
        chatContext.setUser(userService.getLoggedInUser());
    }

    @PostMapping("/chat/direct/sendMessage/{chatRoomId}")
    public ResponseEntity sendDirectMessage(@RequestParam String chatRoomId, @RequestBody ChatMessage chatMessage) {
        try{
            directChatService.sendMessage(chatMessage);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
