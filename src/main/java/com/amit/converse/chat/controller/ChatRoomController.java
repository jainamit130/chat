package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.dto.CreateGroupResponse;
import com.amit.converse.chat.dto.GroupDetails;
import com.amit.converse.chat.dto.GroupMembersRequest;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import com.amit.converse.chat.service.ChatMessageService;
import com.amit.converse.chat.service.GroupServiceOld;
import com.amit.converse.chat.service.WebSocketMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final GroupServiceOld groupServiceOld;
    private final ChatMessageService chatMessageService;
    private final WebSocketMessageService webSocketMessageService;

    @PostMapping("/groups/create")
    public ResponseEntity<CreateGroupResponse> createGroup(@RequestBody CreateGroupRequest request) {
        try {
            Map.Entry<String, Boolean> result = groupServiceOld.createGroup(request.getGroupName(), request.getChatRoomType(), request.getCreatedById(), request.getMembers());
            String createdChatRoomId = result.getKey();
            boolean isAlreadyPresent = result.getValue();
            ChatMessage savedMessage = null;
            if(isAlreadyPresent) {
                savedMessage = chatMessageService.addMessage(createdChatRoomId, request.getLatestMessage(), false);
                webSocketMessageService.sendMessage(createdChatRoomId,savedMessage);
                groupServiceOld.sendNewChatStatusToDeletedMembers(createdChatRoomId);
            } else {
                if(request.getChatRoomType()== ChatRoomType.DIRECT){
                    savedMessage = chatMessageService.addMessage(createdChatRoomId, request.getLatestMessage(),true);
                    groupServiceOld.sendNewChatStatusToMember(createdChatRoomId);
                }
            }
            CreateGroupResponse groupResponse = CreateGroupResponse.builder().chatRoomId(createdChatRoomId).build();
            return new ResponseEntity<>(groupResponse,HttpStatus.OK);
        } catch (IllegalArgumentException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/add/{chatRoomId}")
    public ResponseEntity<Boolean> addMembersToGroup(@PathVariable String chatRoomId, @RequestBody GroupMembersRequest request) {
        try {
            return ResponseEntity.ok(groupServiceOld.addMembers(chatRoomId, request.getActionedBy(), request.getMemberIds()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/remove/{chatRoomId}")
    public ResponseEntity<Boolean> removeMembersFromGroup(
            @PathVariable String chatRoomId,
            @RequestBody GroupMembersRequest request) {
        try {
            return new ResponseEntity<>(
                    groupServiceOld.removeMembers(chatRoomId, request.getActionedBy(), request.getMemberIds()),
                    HttpStatus.OK
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @PostMapping("/groups/delete/{chatRoomId}")
    public ResponseEntity<Boolean> deleteGroup(@PathVariable String chatRoomId, @RequestBody String userId) {
        try {
            return new ResponseEntity(groupServiceOld.deleteChat(chatRoomId, userId),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/clearChat/{chatRoomId}")
    public ResponseEntity<Boolean> clearChat(@PathVariable String chatRoomId, @RequestBody String userId) {
        try {
            return new ResponseEntity(groupServiceOld.clearChat(chatRoomId,userId),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/groups/details/{chatRoomId}")
    public ResponseEntity<GroupDetails> getGroupDetails(@PathVariable String chatRoomId) {
        try {
            return new ResponseEntity(groupServiceOld.getGroupDetails(chatRoomId),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
