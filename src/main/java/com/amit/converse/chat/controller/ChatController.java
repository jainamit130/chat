package com.amit.converse.chat.controller;

import com.amit.converse.chat.config.util.SecurityContextUtil;
import com.amit.converse.chat.dto.ChatRoomData;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.UserService;
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
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
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

    @MessageMapping("/chat/send/message/{chatRoomId}")
    public void sendMessage(@DestinationVariable String chatRoomId, ChatMessage message, Principal principal) {
        try {
            SecurityContextUtil.ensureContextFromPrincipal(principal);
            message.setChatRoomId(chatRoomId);
            chatMessageServiceFactory.getMessageServiceFactory(chatRoomId).sendMessage(message);
        } catch (IllegalArgumentException | InterruptedException e) {
            System.err.println("Error sending message: " + e.getMessage());
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
    public ResponseEntity clearChat(@PathVariable String chatRoomId) {
        try {
            clearChatService.clearChatAndSave();
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
