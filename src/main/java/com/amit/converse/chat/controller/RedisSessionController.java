package com.amit.converse.chat.controller;

import com.amit.converse.chat.Redis.DirectChatRedisTransitionService;
import com.amit.converse.chat.Redis.GroupChatRedisTransitionService;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.dto.OnlineUsersDto;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/converse/user/")
public class RedisSessionController {
    private final UserContext userContext;
    private final UserService userService;
    private final DirectChatRedisTransitionService directChatRedisTransitionService;
    private final GroupChatRedisTransitionService groupChatRedisTransitionService;

    private void transit() {
        User user = userService.getUserById(userContext.getUserId());
        user.transit();
        userService.processUserToDB(userContext.getUser());
    }

    @PostMapping("/state/transit")
    public ResponseEntity transitState() {
        transit();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/save/direct/active/{chatRoomId}")
    public ResponseEntity<IOnlineUsersDTO> saveActiveDirectChat(@RequestParam String chatRoomId) {
        return new ResponseEntity<IOnlineUsersDTO>(directChatRedisTransitionService.transitAndGetOnlineUsers(), HttpStatus.OK);
    }

    @PostMapping("/save/group/active/{chatRoomId}")
    public ResponseEntity<IOnlineUsersDTO> saveActiveGroupChat(@RequestParam String chatRoomId) {
        return new ResponseEntity<IOnlineUsersDTO>(groupChatRedisTransitionService.transitAndGetOnlineUsers(), HttpStatus.OK);
    }

}
