package com.amit.converse.chat.controller;

import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.GroupContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.ChatRoom.GroupService;
import com.amit.converse.chat.service.JoinService;
import com.amit.converse.chat.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupContext groupContext;
    private final UserContext userContext;
    private final GroupService groupService;
    private final UserService userService;
    private final JoinService joinService;

    @ModelAttribute
    public void populateChatContext(@RequestParam String groupId) {
        groupContext.setGroupChat(groupService.getGroupById(groupId));
        userContext.setUser(userService.getLoggedInUser());
    }

    @PostMapping("/groups/joinChat/{chatRoomId}")
    public ResponseEntity<Boolean> joinChat(@RequestParam String chatRoomId, @RequestBody List<String> userIds) {
        try {
            joinService.join(userIds);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/exitChat/{chatRoomId}")
    public ResponseEntity<Boolean> exitChat(@ModelAttribute ChatContext chatContext) {
        try {
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
