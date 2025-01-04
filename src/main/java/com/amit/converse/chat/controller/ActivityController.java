package com.amit.converse.chat.controller;

import com.amit.converse.chat.service.WebSocketMessageService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@AllArgsConstructor
public class ActivityController {

    private final WebSocketMessageService webSocketMessageService;
    private final Set<String> typingUsers = ConcurrentHashMap.newKeySet();

    @MessageMapping("/typing/{chatRoomId}")
    public void handleTypingEvent(String username, @DestinationVariable String chatRoomId) {
        typingUsers.add(username);
        webSocketMessageService.sendTypingStatusToGroup(new ArrayList<>(typingUsers),chatRoomId);
    }

    @MessageMapping("/stopTyping/{chatRoomId}")
    public void handleStopTypingEvent(String username, @DestinationVariable String chatRoomId) {
        typingUsers.remove(username);
        webSocketMessageService.sendTypingStatusToGroup(new ArrayList<>(typingUsers),chatRoomId);
    }

}