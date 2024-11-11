package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.dto.CreateGroupResponse;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.ChatRoomType;
import com.amit.converse.chat.service.ChatService;
import com.amit.converse.chat.service.GroupService;
import com.amit.converse.chat.service.WebSocketMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final GroupService groupService;
    private final ChatService chatService;
    private final WebSocketMessageService webSocketMessageService;

    @PostMapping("/groups/create")
    public ResponseEntity<CreateGroupResponse> createGroup(@RequestBody CreateGroupRequest request) {
        try {
            Map.Entry<String, Boolean> result = groupService.createGroup(request.getGroupName(), request.getChatRoomType(), request.getCreatedById(), request.getMembers());
            String createdChatRoomId = result.getKey();
            boolean isAlreadyPresent = result.getValue();
            ChatMessage savedMessage = null;
            if(isAlreadyPresent) {
                savedMessage = chatService.addMessage(createdChatRoomId, request.getLatestMessage(), false);
                webSocketMessageService.sendMessage(createdChatRoomId,savedMessage);
            } else {
                if(request.getChatRoomType()== ChatRoomType.INDIVIDUAL){
                    savedMessage = chatService.addMessage(createdChatRoomId, request.getLatestMessage(),true);
                    groupService.sendNewChatStatusToMember(createdChatRoomId);
                }
            }
            CreateGroupResponse groupResponse = CreateGroupResponse.builder().chatRoomId(createdChatRoomId).build();
            return new ResponseEntity<>(groupResponse,HttpStatus.OK);
        } catch (IllegalArgumentException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/add/{chatRoomId}")
    public ResponseEntity<ChatRoom> addMembersToGroup(@PathVariable String chatRoomId,
                                                      @RequestBody List<String> memberIds) {
        try {
            ChatRoom chatRoom = groupService.addMembers(chatRoomId, memberIds);
            return ResponseEntity.ok(chatRoom);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/remove/{chatRoomId}")
    public ResponseEntity<Boolean> removeMembersFromGroup(@PathVariable String chatRoomId,
                                                           @RequestBody List<String> memberIds) {
        try {
            return new ResponseEntity(groupService.removeMembers(chatRoomId, memberIds),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/delete/{chatRoomId}")
    public ResponseEntity<Boolean> deleteGroup(@PathVariable String chatRoomId, @RequestBody String userId) {
        try {
            return new ResponseEntity(groupService.deleteChat(chatRoomId, userId),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/clearChat/{chatRoomId}")
    public ResponseEntity<Boolean> clearChat(@PathVariable String chatRoomId, @RequestBody String userId) {
        try {
            return new ResponseEntity(groupService.clearChat(chatRoomId,userId),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
