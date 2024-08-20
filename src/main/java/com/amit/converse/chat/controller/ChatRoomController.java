package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.AddMembersRequest;
import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.repository.ChatRoomRepository;
import com.amit.converse.chat.service.ChatService;
import com.amit.converse.chat.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final GroupService groupService;
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/user/{userId}/rooms")
    public List<ChatRoom> getChatRoomsForUser(@PathVariable String userId) {
        return chatRoomRepository.findByUserIdsContains(userId);
    }

    // Endpoint to create a new chat room (group)
    @PostMapping("/groups/create")
    public ResponseEntity<ChatRoom> createGroup(@RequestBody CreateGroupRequest request) {
        try {
            ChatRoom chatRoom = groupService.createGroup(request.getGroupName(), request.getCreatedByUserId(), request.getMembers());
            return ResponseEntity.ok(chatRoom);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Endpoint to add members to an existing chat room
    @PostMapping("/groups/add/{chatRoomId}")
    public ResponseEntity<ChatRoom> addMembersToGroup(@PathVariable String chatRoomId,
                                                      @RequestBody AddMembersRequest request) {
        try {
            ChatRoom chatRoom = groupService.addMembers(chatRoomId, request.getMemberIds());
            return ResponseEntity.ok(chatRoom);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/remove/{chatRoomId}")
    public ResponseEntity<ChatRoom> removeMembersFromGroup(@PathVariable String chatRoomId,
                                                           @RequestBody AddMembersRequest request) {
        try {
            ChatRoom chatRoom = groupService.removeMembers(chatRoomId, request.getMemberIds());
            return ResponseEntity.ok(chatRoom);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
