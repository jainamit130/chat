package com.amit.converse.chat.controller;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.repository.ChatRoomRepository;
import com.amit.converse.chat.service.TestService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final TestService testService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/user/{userId}/rooms")
    public List<ChatRoom> getChatRoomsForUser(@PathVariable String userId) {
        return chatRoomRepository.findByUserIdsContains(userId);
    }

    @MessageMapping("/chat/sendMessage/{chatRoomId}")
    public void sendMessage(@DestinationVariable String chatRoomId, ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            testService.addMessage(chatRoomId, chatMessage);
            messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, chatMessage);
        } catch (IllegalArgumentException e) {
            // Handle the exception as needed
        }
    }
}
