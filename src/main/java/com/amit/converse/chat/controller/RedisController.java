package com.amit.converse.chat.controller;

import com.amit.converse.chat.exceptions.ConverseException;
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
    public void saveLastSeen(@PathVariable String userId, @RequestParam String timestamp, @RequestParam(value = "prevChatRoomId", required = false) String prevChatRoomId) {
        if(userId==null){
            return;
        }
        if(prevChatRoomId!=null){
            redisService.addUserIdToChatRoom(prevChatRoomId,userId);
        }
        redisService.setUserTimestamp(userId, timestamp);
        return;
    }

    @GetMapping("/get/lastSeen/{userId}")
    public Instant getUserLastSeen(@PathVariable String userId) {
        return redisService.getUserTimestamp(userId);
    }

    @PostMapping("/update/lastSeen/{userId}")
    public String updateLastSeen(@PathVariable String userId, @RequestParam(value = "prevChatRoomId", required = false) String prevChatRoomId) {
        if(userId==null){
            throw new ConverseException("User does not exist");
        }
        userService.updateUserLastSeen(userId,redisService.getUserTimestamp(userId));
        if(prevChatRoomId!=null){
            redisService.removeUserFromChatRoom(prevChatRoomId,userId);
        }
        redisService.saveAndRemoveTimestamp(userId);
        return "User marked as offline";
    }

    @PostMapping("/save/activeChatRoom/{chatRoomId}/{userId}")
    public String updateActiveChatRoom(@PathVariable String userId, @PathVariable String chatRoomId, @RequestParam(value = "prevChatRoomId", required = false) String prevChatRoomId) {
        if(prevChatRoomId!=null){
            redisService.removeUserFromChatRoom(prevChatRoomId,userId);
        }
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
