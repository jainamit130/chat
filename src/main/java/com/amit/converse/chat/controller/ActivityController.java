package com.amit.converse.chat.controller;

import com.amit.converse.chat.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@AllArgsConstructor
public class ActivityController {

    private final ActivityService activityService;
    private final Set<String> typingUsers = ConcurrentHashMap.newKeySet();

    @MessageMapping("/typing/{chatRoomId}")
    public void handleTypingEvent(String username) {
        typingUsers.add(username);
        activityService.sendTypingNotification(new ArrayList<>(typingUsers));
    }

    @MessageMapping("/stopTyping/{chatRoomId}")
    public void handleStopTypingEvent(String username) {
        typingUsers.remove(username);
        activityService.sendTypingNotification(new ArrayList<>(typingUsers));
    }

}