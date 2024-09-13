package com.amit.converse.chat.controller;

import com.amit.converse.chat.service.WebSocketMessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

@Controller
@AllArgsConstructor
public class ActivityController {

    private final WebSocketMessageService webSocketMessageService;
    private final Set<String> typingUsers = new HashSet<>();

    @MessageMapping("/typing/{chatRoomId}")
    public void handleTypingEvent(String username, @DestinationVariable String chatRoomId) {
        typingUsers.add(username);
        webSocketMessageService.sendTypingStatusToGroup(typingUsers,chatRoomId);
    }

    @MessageMapping("/stopTyping/{chatRoomId}")
    public void handleStopTypingEvent(String username, @DestinationVariable String chatRoomId) {
        typingUsers.remove(username);
        webSocketMessageService.sendTypingStatusToGroup(typingUsers,chatRoomId);
    }

}