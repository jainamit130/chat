package com.amit.converse.chat.controller;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.ChatRoom.GroupMessageService;
import com.amit.converse.chat.service.ChatRoom.GroupService;
import com.amit.converse.chat.service.ExitService;
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

    private final ChatContext<ITransactable> groupContext;
    private final GroupService groupService;
    private final GroupMessageService groupMessageService;
    private final UserService userService;
    private final JoinService joinService;
    private final ExitService exitService;

    @ModelAttribute
    public void populateChatContext(@RequestParam String groupId) {
        groupContext.setChatRoom(groupService.getGroupById(groupId));
        groupContext.setUser(userService.getLoggedInUser());
    }

    @PostMapping("/chat/group/sendMessage/{chatRoomId}")
    public ResponseEntity sendGroupMessage(@RequestParam String chatRoomId, @RequestBody ChatMessage chatMessage) {
        try{
            groupMessageService.sendMessage(chatMessage);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/joinChat/{chatRoomId}")
    public ResponseEntity joinChat(@RequestParam String chatRoomId, @RequestBody List<String> userIds) {
        try {
            joinService.join(userIds);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/groups/exitChat/{chatRoomId}")
    public ResponseEntity<Boolean> exitChat(@RequestParam String chatRoomId, @RequestBody List<String> userIds) {
        try {
            exitService.leave(userIds);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
