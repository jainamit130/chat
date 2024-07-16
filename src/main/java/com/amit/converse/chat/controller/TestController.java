package com.amit.converse.chat.controller;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final SimpMessagingTemplate messagingTemplate;

    // Endpoint to add a new message to a chat room
    @PostMapping("/messages")
    public ResponseEntity<String> addMessage(@RequestBody ChatMessage message) {
        try {
            testService.addMessage(message.getChatRoomId(), message);
            messagingTemplate.convertAndSend("/topic/chat/" + message.getChatRoomId(), message);
            return ResponseEntity.ok("Message added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint to create a new chat room (group)
    @PostMapping("/groups")
    public ResponseEntity<ChatRoom> createGroup(@RequestBody CreateGroupRequest request) {
        try {
            ChatRoom chatRoom = testService.createGroup(request.getGroupName(), request.getCreatedByUserId());
            return ResponseEntity.ok(chatRoom);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Endpoint to add members to an existing chat room
    @PostMapping("/groups/{chatRoomId}/members")
    public ResponseEntity<ChatRoom> addMembersToGroup(@PathVariable String chatRoomId,
                                                      @RequestBody AddMembersRequest request) {
        try {
            ChatRoom chatRoom = testService.addMembers(chatRoomId, request.getMemberIds());
            return ResponseEntity.ok(chatRoom);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Request body classes for POST requests

    // Example request body for createGroup endpoint
    static class CreateGroupRequest {
        private String groupName;
        private String createdByUserId;

        // getters and setters
        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getCreatedByUserId() {
            return createdByUserId;
        }

        public void setCreatedByUserId(String createdByUserId) {
            this.createdByUserId = createdByUserId;
        }
    }

    // Example request body for addMembersToGroup endpoint
    static class AddMembersRequest {
        private List<String> memberIds;

        // getters and setters
        public List<String> getMemberIds() {
            return memberIds;
        }

        public void setMemberIds(List<String> memberIds) {
            this.memberIds = memberIds;
        }
    }
}