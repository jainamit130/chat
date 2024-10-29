package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.GroupStatusResponse;
import com.amit.converse.chat.dto.OnlineStatusDto;
import com.amit.converse.chat.dto.UserResponseDto;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.ChatRoomType;
import com.amit.converse.chat.model.OnlineStatus;
import com.amit.converse.chat.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class RedisController {

    private final RedisService redisService;
    private final UserService userService;
    private final GroupService groupService;
    private final MessageProcessingService messageProcessingService;

    @PostMapping("/save/lastSeen/{userId}")
    public void saveLastSeen(@PathVariable String userId, @RequestParam(value = "prevChatRoomId", required = false) String prevChatRoomId) {
        if(userId==null){
            return;
        }
        if(prevChatRoomId!=null){
            redisService.addUserIdToChatRoom(prevChatRoomId,userId);
        }
        redisService.setUser(userId);
        messageProcessingService.sendOnlineStatusToAllChatRooms(userId, OnlineStatus.ONLINE);
        return;
    }

    @PostMapping("/update/lastSeen/{userId}")
    public String updateLastSeen(@PathVariable String userId, @RequestParam(value = "prevChatRoomId", required = false) String prevChatRoomId) {
        if(userId==null){
            throw new ConverseException("User does not exist");
        }
        userService.updateUserLastSeen(userId,Instant.now());
        if(prevChatRoomId!=null){
            redisService.removeUserFromChatRoom(prevChatRoomId,userId);
        }
        redisService.removeUser(userId);
        messageProcessingService.sendOnlineStatusToAllChatRooms(userId, OnlineStatus.OFFLINE);
        return "User marked as offline";
    }

    @PostMapping("/save/activeChatRoom/{chatRoomId}/{userId}")
    public ResponseEntity<GroupStatusResponse> saveActiveChatRoom(@PathVariable String userId, @PathVariable String chatRoomId, @RequestParam(value = "prevChatRoomId", required = false) String prevChatRoomId) {
        if(prevChatRoomId!=null){
            redisService.removeUserFromChatRoom(prevChatRoomId,userId);
        }
        redisService.addUserIdToChatRoom(chatRoomId,userId);
        ChatRoom chatRoom = groupService.getChatRoom(chatRoomId);
        Set<String> onlineUsers = groupService.getOnlineUsersOfGroup(chatRoom);
        Instant lastSeenTimstamp = null;
        // If ChatRoom is Individual get Last Seen
        if(chatRoom.getChatRoomType()== ChatRoomType.INDIVIDUAL)
            lastSeenTimstamp=groupService.getLastSeenTimeStampOfCouterPartUser(chatRoom,userId);
        GroupStatusResponse response = GroupStatusResponse.builder().onlineUsers(onlineUsers).lastSeenTimestamp(lastSeenTimstamp).build();
        return new ResponseEntity<GroupStatusResponse>(response, HttpStatus.OK);
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
