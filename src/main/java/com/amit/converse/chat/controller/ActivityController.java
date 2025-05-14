package com.amit.converse.chat.controller;

import com.amit.converse.chat.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@AllArgsConstructor
@RequestMapping("/converse/chat/")
public class ActivityController {

    private final ActivityService activityService;
    private final Map<String, Set<String>> typingUsersMap = new ConcurrentHashMap<>();

    @MessageMapping("/chat/typing/{chatRoomId}")
    public void handleTypingEvent(@DestinationVariable String chatRoomId, String username) {
        typingUsersMap.get(chatRoomId).add(username);
        activityService.sendTypingNotification(chatRoomId,new ArrayList<>(typingUsersMap.get(chatRoomId)));
    }

    @MessageMapping("/chat/stopTyping/{chatRoomId}")
    public void handleStopTypingEvent(@DestinationVariable String chatRoomId,String username) {
        typingUsersMap.get(chatRoomId).remove(username);
        activityService.sendTypingNotification(chatRoomId,new ArrayList<>(typingUsersMap.get(chatRoomId)));
    }

}