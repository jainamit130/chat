package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.ChatRoomData;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.chatRoom.ChatService;
import com.amit.converse.chat.service.MessageService.ChatMessageServiceFactory;
import com.amit.converse.chat.service.MessageService.DeleteMessageService.ClearChatService;
import com.amit.converse.chat.service.DeleteChatService;
import com.amit.converse.chat.service.MessageService.DeleteMessageService.DeleteMessageForEveryoneService;
import com.amit.converse.chat.service.MessageService.DeleteMessageService.DeleteMessageForMeService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/converse/chat/")
public class ChatController {
    private final ClearChatService clearChatService;
    private final DeleteChatService deleteChatService;
    private final DeleteMessageForMeService deleteMessageForMeService;
    private final DeleteMessageForEveryoneService deleteMessageForEveryoneService;
    private final ChatService chatService;
    private final ChatMessageServiceFactory chatMessageServiceFactory;

    @QueryMapping
    public ChatRoomData getChatRoomData(@Argument String chatRoomId){
        try {
            return chatService.getChatRoomData(chatRoomId);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @PostMapping("/send/message/{chatRoomId}")
    public ResponseEntity sendMessage(@RequestBody ChatMessage message) {
        try {
            chatMessageServiceFactory.getMessageServiceFactory().sendMessage(message);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/delete/messages/me/{chatRoomId}")
    public ResponseEntity deleteMessage(@RequestBody List<String> messageIds) {
        try {
            deleteMessageForMeService.deleteMessageForMe(messageIds);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("delete/messages/everyone/{chatRoomId}")
    public ResponseEntity deleteMessageForEveryone(@RequestBody List<String> messageIds) {
        try {
            deleteMessageForEveryoneService.deleteMessageForEveryone(messageIds);
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
