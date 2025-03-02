package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.ChatRoomData;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.MessageService.ClearChatService;
import com.amit.converse.chat.service.DeleteChatService;
import com.amit.converse.chat.service.MessageService.IDeleteMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/converse/chat/")
public class ChatController {
    private final ClearChatService clearChatService;
    private final DeleteChatService deleteChatService;
    private final IDeleteMessageService deleteMessageService;
    private final ChatService chatService;

    @QueryMapping
    public ChatRoomData getChatRoomData(){
        return chatService.getChatRoomData();
    }

    @PostMapping("/delete/messages/me/{messageId}")
    public ResponseEntity deleteMessage() {
        try {
            deleteMessageService.deleteMessageForMe();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("delete/messages/everyone/{messageId}")
    public ResponseEntity deleteMessageForEveryone() {
        try {
            deleteMessageService.deleteMessageForEveryone();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/clearChat/{chatRoomId}")
    public ResponseEntity clearChat() {
        try {
            clearChatService.clearChat();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/deleteChat/{chatRoomId}")
    public ResponseEntity<Boolean> deleteChat() {
        try {
            deleteChatService.deleteChat();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
