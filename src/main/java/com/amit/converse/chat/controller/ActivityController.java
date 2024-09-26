package com.amit.converse.chat.controller;

import com.amit.converse.chat.service.GroupService;
import com.amit.converse.chat.service.RedisService;
import com.amit.converse.chat.service.WebSocketMessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@AllArgsConstructor
public class ActivityController {

    private final WebSocketMessageService webSocketMessageService;
    private final GroupService groupService;
    private final Set<String> typingUsers = ConcurrentHashMap.newKeySet();
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

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


    @MessageMapping("/subscribe/{chatRoomId}")
    public void subscribeToOnlineUsers(@DestinationVariable String chatRoomId, String username) {
        webSocketMessageService.subscribeUser(chatRoomId, username);

        if (webSocketMessageService.getSubscriptionCount(chatRoomId) == 1) {
            checkAndSendOnlineUsers(chatRoomId);
        }
    }

    @MessageMapping("/unsubscribe/{chatRoomId}")
    public void unsubscribeFromOnlineUsers(@DestinationVariable String chatRoomId, String username) {
        webSocketMessageService.unsubscribeUser(chatRoomId, username);
    }

    private void checkAndSendOnlineUsers(String chatRoomId) {
        Set<String> currentOnlineUsers = groupService.getOnlineUsersOfGroup(chatRoomId);

        if (!currentOnlineUsers.equals(onlineUsers)) {
            onlineUsers.clear();
            onlineUsers.addAll(currentOnlineUsers);
            webSocketMessageService.sendOnlineStatusToGroup(currentOnlineUsers, chatRoomId);
        }
    }
}