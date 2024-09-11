package com.amit.converse.chat.controller;

import com.amit.converse.chat.service.RedisService;
import com.amit.converse.chat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class RedisController {

    private final RedisService redisService;
    private final UserService userService;

    @PostMapping("/save/lastSeen/{userId}")
    public void saveLastSeen(@PathVariable String userId, @RequestParam String timestamp) {
        redisService.saveUserTimestamp(userId, timestamp);
        return;
    }

    @GetMapping("/get/lastSeen/{userId}")
    public Instant getUserLastSeen(@PathVariable String userId) {
        return redisService.getUserTimestamp(userId);
    }

    @PostMapping("/update/lastSeen/{userId}")
    public String updateLastSeen(@PathVariable String userId) {
        userService.updateUserLastSeen(userId,redisService.getUserTimestamp(userId));
        redisService.removeTimestamp(userId);
        return "User marked as offline";
    }

    @PostMapping("/save/activeChatRoom/{chatRoomId}/{userId}")
    public String updateActiveChatRoom(@PathVariable String userId, @PathVariable String chatRoomId) {
        redisService.addUserIdToChatRoom(chatRoomId,userId);
        return "Data saved";
    }

    @GetMapping("/get/activeChatRoom/{chatRoomId}")
    public Boolean isChatRoomActive(@PathVariable String userId, @PathVariable String chatRoomId) {
        return redisService.isUserInChatRoom(userId,chatRoomId);
    }

    @PostMapping("/update/activeChatRoom/{chatRoomId}/{userId}")
    public void removeUserFromChatRoom(@PathVariable String userId, @PathVariable String chatRoomId) {
        redisService.removeUserFromChatRoom(chatRoomId,userId);
        return;
    }
}
