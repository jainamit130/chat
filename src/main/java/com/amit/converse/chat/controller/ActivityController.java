package com.amit.converse.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

@Controller
public class ActivityController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private final Set<String> typingUsers = new HashSet<>();

    @MessageMapping("/typing/{chatRoomId}")
    public void handleTypingEvent(String username, @DestinationVariable String chatRoomId) {
        typingUsers.add(username);
        messagingTemplate.convertAndSend("/topic/typing/" + chatRoomId, typingUsers);
    }

    @MessageMapping("/stopTyping/{chatRoomId}")
    public void handleStopTypingEvent(String username, @DestinationVariable String chatRoomId) {
        typingUsers.remove(username);
        messagingTemplate.convertAndSend("/topic/typing/" + chatRoomId, typingUsers);
    }

}